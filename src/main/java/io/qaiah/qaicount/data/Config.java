package io.qaiah.qaicount.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;


public class Config {
    //config values
    private long channelId;
    private boolean enabled;
    private final String token;
    private final String prefix;

    @JsonCreator
    public Config() {
        this.channelId = 0L;
        this.enabled = true;
        this.token = "null";
        this.prefix = "c!";
    }

    @JsonGetter
    public String getToken() {
        return token;
    }

    @JsonGetter
    public boolean isEnabled() {
        return enabled;
    }

    @JsonGetter
    public long getChannelId() {
        return channelId;
    }

    @JsonGetter
    public String getPrefix() {
        return prefix;
    }

    @JsonSetter
    public void setChannelId(long id) {
        this.channelId = id;
        JsonHelper.save();
    }

    public void enable() {
        this.enabled = true;
        JsonHelper.save();
    }

    public void disable() {
        this.enabled = false;
        JsonHelper.save();
    }
}
