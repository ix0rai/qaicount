package io.qaiah.qaicount.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class JsonObject {
    @JsonProperty("token")
    private final String token;
    @JsonProperty("prefix")
    private final String prefix;
    @JsonProperty("guilds")
    private final Map<Long, Counter> counters;

    @JsonCreator
    public JsonObject() {
        this.prefix = "c!";
        token = "";
        counters = new HashMap<>();
    }

    @JsonGetter
    public String getToken() {
        return token;
    }

    @JsonGetter
    public String getPrefix() {
        return prefix;
    }

    @JsonGetter
    public Map<Long, Counter> getCounters() {
        return counters;
    }
}
