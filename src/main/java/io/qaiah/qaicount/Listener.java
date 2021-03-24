package io.qaiah.qaicount;

import io.qaiah.qaicount.data.JsonHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        final String contentRaw = event.getMessage().getContentRaw();
        final MessageChannel channel = event.getChannel();
        final long id = event.getGuild().getIdLong();
        //if we have a number
        if (isNumber(contentRaw) && channel.equals(Main.getConfiguredChannel(id))) {
            Main.getCounter(id).handle(Integer.parseInt(contentRaw), event);
        //otherwise we handle other commands
        } else if (contentRaw.startsWith(Main.getPrefix())) {
            final String[] content = contentRaw.split(Main.getPrefix())[1].split(" ");
            switch (content[0]) {
                case "cfg":
                case "config":
                    //user must be present in our admin list
                    if (!Main.isAdmin(event.getAuthor().getIdLong())) {
                        return;
                    }

                    switch (content[1]) {
                        case "channel":
                            try {
                                long channelId = channel.getIdLong();
                                if (content.length >= 3 && isNumber(content[2])) {
                                    channelId = Long.parseLong(content[2]);
                                }
                                Main.getConfig(id).setChannelId(channelId);
                                JsonHelper.save();
                                channel.sendMessage(successEmbed("set counting channel to <#" + channelId + ">")).queue();
                            } catch (NumberFormatException e) {
                                channel.sendMessage(errorEmbed("failed to set channel: id provided is invalid (`NumberFormatException`)")).queue();
                            }
                            break;
                        case "enable":
                            Main.enable(id);
                            channel.sendMessage(successEmbed("enabled counting")).queue();
                            break;
                        case "disable":
                            Main.disable(id);
                            channel.sendMessage(successEmbed("disabled counting")).queue();
                            break;
                        case "admin":
                        case "admins":
                            if (content.length >= 4) {
                                if (content[2].equals("add")) {
                                    try {
                                        Main.getAdmins().add(Long.parseLong(content[3]));
                                        channel.sendMessage(successEmbed("added admin: <@" + content[3] + ">")).queue();
                                    } catch (NumberFormatException e) {
                                        channel.sendMessage(errorEmbed(content[3] + " is not a user id (`NumberFormatException`)")).queue();
                                    }
                                } else if (content[2].equals("remove")) {
                                    try {
                                        Main.getAdmins().remove(Long.parseLong(content[3]));
                                        channel.sendMessage(successEmbed("removed admin: <@" + content[3] + ">")).queue();
                                    } catch (NumberFormatException e) {
                                        channel.sendMessage(errorEmbed(content[3] + " is not a user id (`NumberFormatException`)")).queue();
                                    }
                                } else {
                                    channel.sendMessage(errorEmbed("unrecognised argument")).queue();
                                }
                            } else {
                                channel.sendMessage(errorEmbed("not enough arguments")).queue();
                            }
                            break;
                    }
                    break;
                case "info":
                    channel.sendMessage(successEmbed(Main.getCounter(id).getCurrentRun().toString())).queue();
                    break;
                case "help":
                    channel.sendMessage(new EmbedBuilder()
                            .setColor(0x00FF00)
                            .setAuthor("qaicount")
                            .setTitle("help")
                            .addField("`cfg [subcommand]`",
                                    "commands:\n" +
                                    "`channel [channel id or leave empty to use current channel]`: set the counting channel\n" +
                                    "`enable`: turn on counting for this guild\n" +
                                    "`disable`: turn off counting for this guild", false)
                            .addField("`info`", "info about the currently going count", false)
                            .addField("`help`", "display this message", false)
                            .addField("`runs [amount to retrieve or \"all\" to get all (defaults to 5)]`", "show past runs", false)
                            .addField("`best`", "info about the best run so far", false)
                            .setFooter("made by ioxom")
                            .build()
                    ).queue();
                    break;
                case "runs":
                case "history":
                    if (content.length > 1) {
                        if (content[1].equals("all")) {
                            channel.sendMessage(successEmbed(Main.getCounter(id).getPastRunsAsString(Integer.MAX_VALUE))).queue();
                        } else {
                            try {
                                channel.sendMessage(successEmbed(Main.getCounter(id).getPastRunsAsString(Integer.parseInt(content[1])))).queue();
                            } catch (NumberFormatException e) {
                                channel.sendMessage(errorEmbed("could not parse argument \"number of runs to retrieve (`args[1]`)\" to integer (`NumberFormatException`)")).queue();
                            }
                        }
                    } else {
                        channel.sendMessage(successEmbed(Main.getCounter(id).getPastRunsAsString(5))).queue();
                    }
                    break;
                case "best":
                    channel.sendMessage(successEmbed(Main.getCounter(id).getBestRun().toString())).queue();
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
