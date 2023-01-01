package lessons;

import lessons.lesson1.TaxComputer;
import org.junit.Test;

import java.util.function.Function;

public class Lesson2Test {
    @Test
    public void withClass() {
        TaxComputer tc9 = new TaxComputer(0.09);
        double price = tc9.compute(120);
        System.out.println(price);
    }

    @Test
    public void withFunction() {
        double tax = 0.09;
        Function<Double, Function<Double, Double>> addTax
                = taxRate -> price -> price + price * taxRate;
        System.out.println(addTax.apply(tax).apply(120.00));

        // Partially applying
        Function<Double, Double> tc9 = addTax.apply(0.09);
        double price = tc9.apply(12.0);
    }
}
