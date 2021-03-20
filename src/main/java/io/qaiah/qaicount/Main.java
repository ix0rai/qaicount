package io.qaiah.qaicount;

import io.qaiah.qaicount.data.Config;
import io.qaiah.qaicount.data.CountingData;
import io.qaiah.qaicount.data.JsonHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.security.auth.login.LoginException;

public class Main {

    private static JDA api;

    private static final CountingData DATA = JsonHelper.read();

    public static void main(String[] args) {

        try {
            api = JDABuilder.createDefault(DATA.getConfig().getToken()).build();
            api.addEventListener(new Listener());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static CountingData getData() {
        return DATA;
    }

    public static Config getConfig() {
        return DATA.config;
    }

    public static void enable() {
        DATA.config.enable();
    }

    public static void disable() {
        DATA.config.disable();
    }

    public static MessageChannel getConfiguredChannel() {
        return api.getTextChannelById(getConfig().getChannelId());
    }
}
