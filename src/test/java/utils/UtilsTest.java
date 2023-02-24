package utils;

import org.junit.Test;

public class UtilsTest {
    @Test
    public void resolveTypeTest(){
        Bar b = new Bar();
        System.out.println(b.getEntityClazz());    }
}

class Foo<T> {
    public Class<T> getEntityClazz() {
        return GenericUtils.resolveTypeArgument(this.getClass(), Foo.class, 1);
    }
}

class Bar extends Foo<String> {}
