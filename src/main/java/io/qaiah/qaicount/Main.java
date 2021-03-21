package io.qaiah.qaicount;

import io.qaiah.qaicount.data.Config;
import io.qaiah.qaicount.data.Counter;
import io.qaiah.qaicount.data.JsonHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.security.auth.login.LoginException;

public class Main {

    private static JDA api;

    private static final Counter COUNTER = JsonHelper.read();

    public static void main(String[] args) {

        try {
            api = JDABuilder.createDefault(COUNTER.getConfig().getToken()).build();
            api.addEventListener(new Listener());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static Counter getCounter() {
        return COUNTER;
    }

    public static Config getConfig() {
        return COUNTER.getConfig();
    }

    public static void enable() {
        COUNTER.getConfig().enable();
    }

    public static void disable() {
        COUNTER.getConfig().disable();
    }

    public static MessageChannel getConfiguredChannel() {
        return api.getTextChannelById(getConfig().getChannelId());
    }
}
