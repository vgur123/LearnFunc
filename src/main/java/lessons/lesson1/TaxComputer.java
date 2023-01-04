package lessons.lesson1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaxComputer {
    private final double rate;
    public TaxComputer(double rate) {
        this.rate = rate;
    }
    public double compute(double price) {
        return price * rate + price;
    }

}
