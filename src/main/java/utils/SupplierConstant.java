package utils;

import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class SupplierConstant<T> implements Supplier<T> {
    private final T value;

    @Override
    public T get() {
        return value;
    }
}
