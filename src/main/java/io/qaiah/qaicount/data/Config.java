package io.qaiah.qaicount.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;


public class Config {
    //config values
    @JsonProperty("channelId")
    private long channelId;
    @JsonProperty("enabled")
    private boolean enabled;

    @JsonCreator
    public Config() {
        this.channelId = 0L;
        this.enabled = true;
    }

    @JsonGetter
    public boolean isEnabled() {
        return enabled;
    }

    @JsonGetter
    public long getChannelId() {
        return channelId;
    }

    @JsonSetter
    public void setChannelId(long id) {
        this.channelId = id;
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
