package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class JsonReader<T> {
    private static final ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    private final ObjectReader objectReader;

    public JsonReader(Class<T> tClass){
        objectReader = objectMapper.readerFor(tClass);
    }

    public JsonReader(TypeReference<T> typeReference){
        objectReader = objectMapper.readerFor(typeReference);
    }

    public T read(String str) throws JsonProcessingException {
        if (str==null) return null;
        return objectReader.readValue(str);
    }
}
