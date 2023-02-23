package utils;

import org.springframework.core.GenericTypeResolver;

public class GenericUtils {
    public static <T> Class <T> resolveTypeArgument(Class<?> clazz, Class<?> iclazz, int arg){
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(clazz, iclazz[arg]);
    }
}
