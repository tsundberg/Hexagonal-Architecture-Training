package se.thinkcode.infrastructure;

import com.google.gson.Gson;

public class Serializer  {
    private final Gson gson = new Gson();

    public String serialize(Object model) {
        return gson.toJson(model);
    }
}
