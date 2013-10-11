package chaschev.json;

public interface Mapper {
    String toJSON(Object obj);

    <T> T fromJSON(String s, Class<T> aClass);
}