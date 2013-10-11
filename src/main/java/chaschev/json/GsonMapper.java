package chaschev.json;

import com.google.gson.Gson;

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
}
