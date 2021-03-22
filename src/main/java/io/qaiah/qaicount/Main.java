package io.qaiah.qaicount;

import io.qaiah.qaicount.data.Config;
import io.qaiah.qaicount.data.Counter;
import io.qaiah.qaicount.data.JsonHelper;
import io.qaiah.qaicount.data.JsonData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.security.auth.login.LoginException;
import java.util.Map;

public class Main {

    private static JDA api;

    private static final JsonData JSON_DATA = JsonHelper.read();

    public static void main(final String[] args) {
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

    public static Counter getCounter(final long id) {
        if (!getCounters().containsKey(id)) {
            getCounters().put(id, new Counter());
        }

        return getCounters().get(id);
    }

    public static Config getConfig(final long id) {
        return getCounter(id).getConfig();
    }

    public static String getPrefix() {
        return JSON_DATA.getPrefix();
    }

    public static void enable(final long id) {
        getConfig(id).enable();
    }

    public static void disable(final long id) {
        getConfig(id).disable();
    }

    public static MessageChannel getConfiguredChannel(long id) {
        return api.getTextChannelById(getConfig(id).getChannelId());
    }

    public static JsonData getJsonData() {
        return JSON_DATA;
    }
}
