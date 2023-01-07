package lessons;

import lessons.lesson3.model.Category;
import lessons.lesson3.model.Transaction;
import lessons.lesson3.service.CategorizationService;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/* very nice article
    https://reflectoring.io/java-completablefuture/
 */
public class Lesson3Test {

    @Test
    public void synchronousTest()  {
        long start = System.currentTimeMillis();
         List<Category> categories = Stream.of(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"))
                .map(CategorizationService::categorizeTransaction)
                .collect(toList());
        long end = System.currentTimeMillis();

        System.out.printf("The operation took %s ms%n", end - start);
        System.out.println("Categories are: " + categories);
    }

    @Test
    public void parallelTest()  {
        long start = System.currentTimeMillis();
        List<Category> categories = Stream.of(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"))
                .parallel()
                .map(CategorizationService::categorizeTransaction)
                .collect(toList());
        long end = System.currentTimeMillis();

        System.out.printf("The operation took %s ms%n", end - start);
        System.out.println("Categories are: " + categories);
    }

    @Test
    public void completableFutureTest()  {
        Executor executor = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        List<CompletableFuture<Category>> futureCategories = Stream.of(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"))
                .map(transaction -> CompletableFuture.supplyAsync(
                        () -> CategorizationService.categorizeTransaction(transaction), executor)
                )
                .collect(toList());


        List<Category> categories = futureCategories.stream()
                .map(CompletableFuture::join)
                .collect(toList());
        long end = System.currentTimeMillis();

        System.out.printf("The operation took %s ms%n", end - start);
        System.out.println("Categories are: " + categories);
    }

    @Test
    public void webFluxTest()  {
        long start = System.currentTimeMillis();

        Flux<Category> fluxCategories = Flux.just(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"))
                .log()
                .map(transaction -> CategorizationService.categorizeTransaction(transaction));

        fluxCategories.subscribe(System.out::println);

        long end = System.currentTimeMillis();

        System.out.printf("The operation took %s ms%n", end - start);

    }

}
