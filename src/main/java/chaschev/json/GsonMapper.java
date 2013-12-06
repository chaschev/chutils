package chaschev.json;

import com.google.gson.Gson;

import java.util.Map;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public class GsonMapper implements Mapper {
    protected final Gson gson = new Gson();

    @Override
    public String toJSON(Object obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T fromJSON(String s, Class<T> aClass) {
        return gson.fromJson(s, aClass);
    }

    @Override
    public Map<String, Object> toMap(String json) {
        throw new UnsupportedOperationException("todo GsonMapper.toMap");
    }

    @Override
    public Map<String, String> toStringMap(String json) {
        throw new UnsupportedOperationException("todo GsonMapper.toStringMap");
    }

    @Override
    public JacksonMapper prettyPrint(boolean b) {
        throw new UnsupportedOperationException("todo GsonMapper.prettyPrint");
    }
}
