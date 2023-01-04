package lessons.lesson3;

import lessons.lesson3.model.Vehicle;
import lessons.lesson3.model.VehicleDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CompFuture {

    public Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }

    public Future<String> calculateAsync2() throws InterruptedException {
        CompletableFuture<String> future
                = CompletableFuture.supplyAsync(() -> "Hello");
        return future;
    }

    /*
    We want the database call to be asynchronous and create a Mono object
    which would react to the database call completion.
    This is accomplished with the help of CompletableFuture.
     */
    public Mono<VehicleDto> createVehicleAsync(VehicleDto vehicleDto) {
        CompletableFuture<VehicleDto> future = CompletableFuture.supplyAsync(() -> {
            Vehicle vehicleDomain = new Vehicle();
            vehicleDomain.setModel(vehicleDto.getModel());
            vehicleDomain.setMade(vehicleDto.getMade());
            vehicleDomain.setColor(vehicleDto.getColor());
            vehicleDomain.setVin(vehicleDto.getVin());
            vehicleDomain.setYear(vehicleDto.getYear());
            //vehicleDomain = vehicleRepo.save(vehicleDomain);
            vehicleDto.setId(vehicleDomain.getId());
            return vehicleDto;
        });
        Mono<VehicleDto> monoFromFuture = Mono.fromFuture(future);

        return monoFromFuture;
    }

    /*
    The way we are generating Flux is by creating one explicitly and then emitting the objects
    generated by the future. Once we have the list of vehicle objects available,
    we use a callback function of CompletableFuture — whenComplete.
    Inside this callback, we emit the Vehicle objects received from the future.
     */
    public Flux<VehicleDto> getVehicleListAsync() {
        return Flux.create((emitter) -> {
            CompletableFuture<List<VehicleDto>> future = CompletableFuture.supplyAsync( () -> {
                List<Vehicle> vehicleList = vehicleRepo.findAll(PageRequest.of(0, 1000)).getContent();
                List<VehicleDto> vehicleDtoList = vehicleList.parallelStream().map( (vehicle) -> {
                    return new VehicleDto().setColor(vehicle.getColor())
                            .setId(vehicle.getId())
                            .setMake(vehicle.getMake())
                            .setModel(vehicle.getModel())
                            .setVin(vehicle.getVin())
                            .setYear(vehicle.getYear());
                }).collect(Collectors.toList());
                return vehicleDtoList;
            });
            future.whenComplete( (vehicleDtoList, exception) -> {
                if (exception == null) {
                    vehicleDtoList.forEach(emitter::next);
                    emitter.complete();
                } else {
                    emitter.complete();
                }
            });
        });
    }
}
