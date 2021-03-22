package io.qaiah.qaicount.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class CountRun {
    private int number;
    private long lastCounter;
    private final List<Long> counters;

    @JsonCreator
    public CountRun() {
        number = 0;
        lastCounter = 0L;
        counters = new ArrayList<>();
    }

    public void increase(final long id, final Message msg) {
        number ++;

        addCounter(id);
        lastCounter = id;
        msg.addReaction("\u2705").queue();
        JsonHelper.save();
    }

    private void addCounter(final long id) {
        if (id != 0 && !counters.contains(id)) {
            counters.add(id);
        }
    }

    public void end() {
        addCounter(lastCounter);
    }

    @Override
    public String toString() {
        StringBuilder participants = new StringBuilder();
        for (int i = 0; i < counters.size(); i++) {
            final long counter = counters.get(i);
            if (i == 0) {
                participants.append("<@").append(counter).append(">");
            } else if (i == counters.size() - 1) {
                participants.append(", <@").append(counter).append(">;");
            } else {
                participants.append(", <@").append(counter).append(">");
            }
        }

        return "last number counted: " + number
                + "\nlast counter: " + (lastCounter == 0? "no counters yet" : "<@" + lastCounter + ">")
                + "\nparticipants: " + (participants.toString().equals("") ? "no participants yet" : participants.toString());
    }

    @JsonGetter
    public int getNumber() {
        return number;
    }

    @JsonGetter
    public long getLastCounter() {
        return lastCounter;
    }

    @JsonGetter
    public List<Long> getCounters() {
        return counters;
    }
}
