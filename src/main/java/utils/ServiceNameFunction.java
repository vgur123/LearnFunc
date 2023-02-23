package utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.function.Function;

@Data
public class ServiceNameFunction implements Function<Class<?>, String> {
    @Getter
    @Setter
    protected static ServiceNameFunction instanceDefault =new ServiceNameFunction();
    protected Set<String> excludes = Set.of("Impl","Flow","Service", "Monitored");
    @Override
    public String apply(Class<?> aClass) {
        String str = aClass.getSimpleName();
        for(String exclede: excludes){
            str=str.replaceAll(exclede,"");
        }
        return str;
    }
}
