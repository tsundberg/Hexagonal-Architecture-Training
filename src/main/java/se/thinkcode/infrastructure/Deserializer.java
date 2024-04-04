package se.thinkcode.infrastructure;

import com.google.gson.Gson;

public class Deserializer {
    private final Gson gson = new Gson();

    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
