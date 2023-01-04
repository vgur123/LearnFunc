package lessons;

import lessons.lesson1.TaxComputer;
import lessons.lesson3.CompFuture;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Lesson3Test {

    @Test
    public void future1() throws InterruptedException, ExecutionException {
        CompFuture compFuture = new CompFuture();
        Future<String> completableFuture = compFuture.calculateAsync();
        System.out.println(completableFuture.get());
    }

    @Test
    public void future2() throws InterruptedException, ExecutionException {
        CompFuture compFuture = new CompFuture();
        Future<String> completableFuture = compFuture.calculateAsync2();
        System.out.println(completableFuture.get());
    }

}
