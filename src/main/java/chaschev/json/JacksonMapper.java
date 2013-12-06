package chaschev.json;

import chaschev.util.Exceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.Map;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public class JacksonMapper implements Mapper {
    public static final TypeReference<Map<String, Object>> MAP_TYPE_REF = new TypeReference<Map<String, Object>>() {};
    public static final TypeReference<Map<String, String>> STRING_MAP_TYPE_REF = new TypeReference<Map<String, String>>() {};

    protected final ObjectMapper mapper = new ObjectMapper();
    protected ObjectWriter writer = mapper.writer();

    @Override
    public JacksonMapper prettyPrint(boolean b){
        writer = b ? mapper.writerWithDefaultPrettyPrinter() : mapper.writer();
        return this;
    }

    @Override
    public String toJSON(Object obj) {
        try {
            return writer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw Exceptions.runtime(e);
        }
    }

    @Override
    public <T> T fromJSON(String s, Class<T> aClass) {
        try {
            final ObjectReader reader = mapper.reader(aClass);

            return reader.readValue(s);
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
    }

    public Map<String, Object> toMap(String json){
        try {
            return mapper.readValue(json, MAP_TYPE_REF);
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
    }

    public Map<String, String> toStringMap(String json){
        try {
            return mapper.readValue(json, STRING_MAP_TYPE_REF);
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
    }

//    public String toJson(Map<String, Object> map){
//        try {
//            writer.
//        } catch (IOException e) {
//            throw Exceptions.runtime(e);
//        }
//    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
