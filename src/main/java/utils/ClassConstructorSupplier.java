package utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ClassConstructorSupplier implements Supplier<T> {
    private final Constructor<T> constructor;

    public ClassConstructorSupplier (Class<T> clazz) {
        try {
            if (clazz.isInterface()) {
                throw new UnsupportedOperationException("Type clazz can't be an Interface");
            }
            if (clazz.isEnum()) {
                throw new UnsupportedOperationException("Type clazz can't be an Enum");
            }
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SneakyThrows
    @Override
    public T get() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance();
    }
}
