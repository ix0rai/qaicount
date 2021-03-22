package io.qaiah.qaicount;

import io.qaiah.qaicount.data.Config;
import io.qaiah.qaicount.data.Counter;
import io.qaiah.qaicount.data.JsonHelper;
import io.qaiah.qaicount.data.JsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.security.auth.login.LoginException;
import java.util.Map;

public class Main {

    private static JDA api;

    private static final JsonObject JSON_DATA = JsonHelper.read();

    public static void main(String[] args) {

        try {
            api = JDABuilder.createDefault(JSON_DATA.getToken()).build();
            api.addEventListener(new Listener());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static Map<Long, Counter> getCounters() {
        return JSON_DATA.getCounters();
    }

    public static Counter getCounter(long id) {
        if (!getCounters().containsKey(id)) {
            getCounters().put(id, new Counter());
        }

        return getCounters().get(id);
    }

    public static Config getConfig(long id) {
        return getCounter(id).getConfig();
    }

    public static String getPrefix() {
        return JSON_DATA.getPrefix();
    }

    public static void enable(long id) {
        getConfig(id).enable();
    }

    public static void disable(long id) {
        getConfig(id).disable();
    }

    public static MessageChannel getConfiguredChannel(long id) {
        return api.getTextChannelById(getConfig(id).getChannelId());
    }

    public static JsonObject getJsonData() {
        return JSON_DATA;
    }
}
