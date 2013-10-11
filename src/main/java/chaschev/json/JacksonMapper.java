package chaschev.json;

import chaschev.util.Exceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public class JacksonMapper implements Mapper {
    protected final ObjectMapper mapper = new ObjectMapper();
    protected final ObjectWriter writer = mapper.writer();

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
}
