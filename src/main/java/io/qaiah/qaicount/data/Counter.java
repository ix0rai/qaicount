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

    public void handle(final int input, final MessageReceivedEvent event) {
        if (config.isEnabled()) {
            final long userId = event.getAuthor().getIdLong();
            final Message message = event.getMessage();

            //for the first number we can ignore other criteria as long as the input is correct
            if (currentRun.getNumber() == 0 && input == 1) {
                currentRun.increase(userId, message);
            } else {
                //criteria:
                //1. input is the number after the last one
                //2. a different user is counting this number than the user who counted last
                if (input == currentRun.getNumber() + 1) {
                    if (userId != currentRun.getLastCounter()) {
                        currentRun.increase(userId, message);
                    } else {
                        finishRun();
                        event.getChannel().sendMessageEmbeds(Listener.errorEmbed("user cannot count twice in a row; reset count to 0")).queue();
                    }
                } else {
                    finishRun();
                    event.getChannel().sendMessageEmbeds(Listener.errorEmbed("wrong number; reset count to 0")).queue();
                }
            }
        }
    }

    private void finishRun() {
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
