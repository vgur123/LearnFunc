package lessons;

import org.junit.Test;

import java.util.function.Function;

public class Lesson1Test {

    @Test
    public void t1(){
        compose(triple,square).apply(3);
        System.out.println(compose(triple,square).apply(2));
    }

    Function<Integer, Integer> triple = x -> x * 3;
    Function<Integer, Integer> square = x -> x * x;

    static Function<Integer, Integer> compose(Function<Integer, Integer> f1,
                                              Function<Integer, Integer> f2) {
        return arg -> f1.apply(f2.apply(arg));
    }
}
