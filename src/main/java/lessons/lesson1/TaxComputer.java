package lessons.lesson1;

public class TaxComputer {
    private final double rate;
    public TaxComputer(double rate) {
        this.rate = rate;
    }
    public double compute(double price) {
        return price * rate + price;
    }

}
