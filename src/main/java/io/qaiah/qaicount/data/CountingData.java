package io.qaiah.qaicount.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.qaiah.qaicount.Listener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class CountingData {
    public Config config;
    public CountRun currentRun;
    private final List<CountRun> pastRuns;

    @JsonCreator
    public CountingData() {
        pastRuns = new ArrayList<>();
    }

    public CountingData(Config config, int number) {
        this.config = config;
        this.currentRun = new CountRun(number);
        pastRuns = new ArrayList<>();
    }

    public static CountingData createDefault() {
        return new CountingData(new Config(), 0);
    }

    public void handle(int input, MessageReceivedEvent event) {
        if (config.isEnabled()) {
            final long userId = event.getAuthor().getIdLong();
            final Message message = event.getMessage();

            if (currentRun.getNumber() == 0 && input == 1) {
                currentRun.increase(userId, message);
            } else {
                if (input == currentRun.getNumber() + 1) {
//                    if (userId != currentRun.getLastCounter()) {
                        currentRun.increase(userId, message);
//                    } else {
//                        finishRun();
//                        event.getChannel().sendMessage(Listener.errorEmbed("user cannot count twice in a row; reset count")).queue();
//                    }
                } else {
                    finishRun();
                    event.getChannel().sendMessage(Listener.errorEmbed("wrong number; reset count to 0")).queue();
                }
            }
        }
    }

    public void finishRun() {
        pastRuns.add(currentRun);
        currentRun.end();
        currentRun = new CountRun();
        JsonHelper.save();
    }

    @JsonIgnore
    public String getPastRunsAsString() {
        StringBuilder runs = new StringBuilder("runs: \n" + "\ncurrent run: \n" + currentRun);
        for (int i = pastRuns.size() - 1; i > 0; i--) {
            runs.append("\n\nrun ").append(i).append(":\n").append(pastRuns.get(i));
        }

        return runs.toString();
    }

    @JsonGetter
    public Config getConfig() {
        return config;
    }

    @JsonGetter
    public CountRun getCurrentRun() {
        return currentRun;
    }

    @JsonGetter
    public List<CountRun> getPastRuns() {
        return pastRuns;
    }
}
