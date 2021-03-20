package io.qaiah.qaicount;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {

    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        final String content = event.getMessage().getContentRaw();
        final MessageChannel channel = event.getChannel();
        //if we have a number
        if (isNumber(content) && channel.equals(Main.getConfiguredChannel())) {
            Main.getData().handle(Integer.parseInt(content), event);
        //otherwise we handle other commands
        } else if (content.startsWith(Main.getConfig().getPrefix())) {
            final String[] splitContent = content.split(Main.getConfig().getPrefix())[1].split(" ");
            switch (splitContent[0]) {
                case "cfg":
                case "config":
                    switch (splitContent[1]) {
                        case "channel":
                            try {
                                System.out.println("h");
                                long id = Long.parseLong(splitContent[2]);
                                Main.getConfig().setChannelId(id);
                                channel.sendMessage(successEmbed("set counting channel to <#" + id + ">")).queue();
                            } catch (NumberFormatException e) {
                                channel.sendMessage(errorEmbed("failed to set channel: id provided is invalid (`NumberFormatException`)")).queue();
                            }
                            break;
                        case "enable":
                            Main.enable();
                            channel.sendMessage(successEmbed("enabled counting")).queue();
                            break;
                        case "disable":
                            Main.disable();
                            channel.sendMessage(successEmbed("disabled counting")).queue();
                            break;
                    }
                    break;
                case "info":
                    channel.sendMessage(successEmbed(Main.getData().getCurrentRun().toString())).queue();
                    break;
                case "runs":
                    channel.sendMessage(successEmbed(Main.getData().getPastRunsAsString())).queue();
                    break;
            }
        }
    }

    private static boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static MessageEmbed successEmbed(String description) {
        return new EmbedBuilder()
                .setAuthor("qaicount")
                .setColor(0x00FF00)
                .setDescription(description)
                .setFooter("made by ioxom")
                .build();
    }

    public static MessageEmbed errorEmbed(String description) {
        return new EmbedBuilder()
                .setAuthor("qaicount")
                .setColor(0xB40D0D)
                .setDescription(description)
                .setFooter("made by ioxom")
                .build();
    }
}
