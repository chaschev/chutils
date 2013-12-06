package chaschev.json;

import java.util.Map;

public interface Mapper {
    String toJSON(Object obj);

    <T> T fromJSON(String s, Class<T> aClass);

    Map<String, Object> toMap(String json);
    Map<String, String> toStringMap(String json);

    JacksonMapper prettyPrint(boolean b);
}