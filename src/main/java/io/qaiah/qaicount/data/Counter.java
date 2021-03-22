package io.qaiah.qaicount.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.qaiah.qaicount.Listener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class Counter {
    private final Config config;
    private CountRun currentRun;
    private final List<CountRun> pastRuns;

    @JsonCreator
    public Counter() {
        config = new Config();
        currentRun = new CountRun();
        pastRuns = new ArrayList<>();
    }

    public void handle(int input, MessageReceivedEvent event) {
        if (config.isEnabled()) {
            final long userId = event.getAuthor().getIdLong();
            final Message message = event.getMessage();

            if (currentRun.getNumber() == 0 && input == 1) {
                currentRun.increase(userId, message);
            } else {
                if (input == currentRun.getNumber() + 1) {
                    if (userId != currentRun.getLastCounter()) {
                        currentRun.increase(userId, message);
                    } else {
                        finishRun();
                        event.getChannel().sendMessage(Listener.errorEmbed("user cannot count twice in a row; reset count")).queue();
                    }
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
    public String getPastRunsAsString(int limit) {
        final CountRun bestRun = getBestRun();
        final boolean currentRunBest = bestRun.equals(currentRun);
        final StringBuilder runs = new StringBuilder("runs: \n\n" + (currentRunBest ? "current run (also best run): \n" + currentRun : "current run: \n" + currentRun + "\n\nbest run: \n" + bestRun));

        limit ++;
        for (int i = pastRuns.size() - 1; i > 0; i--) {
            limit--;
            if (limit == 0) break;
            runs.append("\n\nrun ").append(i).append(":\n").append(pastRuns.get(i));
        }

        return runs.toString();
    }

    @JsonIgnore
    public CountRun getBestRun() {
        CountRun bestRun = currentRun;
        for (CountRun run : pastRuns) {
            if (bestRun.getNumber() < run.getNumber()) {
                bestRun = run;
            }
        }

        return bestRun;
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
