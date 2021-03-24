package io.qaiah.qaicount.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonData {
    @JsonProperty("token")
    private final String token;
    @JsonProperty("prefix")
    private final String prefix;
    @JsonProperty("admins")
    private final List<Long> admins;
    @JsonProperty("guilds")
    private final Map<Long, Counter> counters;

    @JsonCreator
    public JsonData() {
        prefix = "c!";
        token = "";
        admins = new ArrayList<>();
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
    public List<Long> getAdmins() {
        return admins;
    }

    @JsonGetter
    public Map<Long, Counter> getCounters() {
        return counters;
    }
}
