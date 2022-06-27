package io.qaiah.qaicount;

import io.qaiah.qaicount.data.JsonHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {
    public static final String CREATOR = "ix0rai";
    public static final String ID = "qaicount";

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
                                channel.sendMessageEmbeds(successEmbed("set counting channel to <#" + channelId + ">")).queue();
                            } catch (NumberFormatException e) {
                                channel.sendMessageEmbeds(errorEmbed("failed to set channel: id provided is invalid (`NumberFormatException`)")).queue();
                            }
                            break;
                        case "enable":
                            Main.enable(id);
                            channel.sendMessageEmbeds(successEmbed("enabled counting")).queue();
                            break;
                        case "disable":
                            Main.disable(id);
                            channel.sendMessageEmbeds(successEmbed("disabled counting")).queue();
                            break;
                        case "admin":
                        case "admins":
                            if (content.length >= 4) {
                                if (content[2].equals("add")) {
                                    try {
                                        Main.getAdmins().add(Long.parseLong(content[3]));
                                        channel.sendMessageEmbeds(successEmbed("added admin: <@" + content[3] + ">")).queue();
                                    } catch (NumberFormatException e) {
                                        channel.sendMessageEmbeds(errorEmbed(content[3] + " is not a user id (`NumberFormatException`)")).queue();
                                    }
                                } else if (content[2].equals("remove")) {
                                    try {
                                        Main.getAdmins().remove(Long.parseLong(content[3]));
                                        channel.sendMessageEmbeds(successEmbed("removed admin: <@" + content[3] + ">")).queue();
                                    } catch (NumberFormatException e) {
                                        channel.sendMessageEmbeds(errorEmbed(content[3] + " is not a user id (`NumberFormatException`)")).queue();
                                    }
                                } else {
                                    channel.sendMessageEmbeds(errorEmbed("unrecognised argument")).queue();
                                }
                            } else {
                                channel.sendMessageEmbeds(errorEmbed("not enough arguments")).queue();
                            }
                            break;
                    }
                    break;
                case "info":
                    channel.sendMessageEmbeds(successEmbed(Main.getCounter(id).getCurrentRun().toString())).queue();
                    break;
                case "help":
                    channel.sendMessageEmbeds(new EmbedBuilder()
                            .setColor(0x00FF00)
                            .setAuthor(ID)
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
                            .setFooter("made by " + CREATOR)
                            .build()
                    ).queue();
                    break;
                case "runs":
                case "history":
                    if (content.length > 1) {
                        if (content[1].equals("all")) {
                            channel.sendMessageEmbeds(successEmbed(Main.getCounter(id).getPastRunsAsString(Integer.MAX_VALUE))).queue();
                        } else {
                            try {
                                channel.sendMessageEmbeds(successEmbed(Main.getCounter(id).getPastRunsAsString(Integer.parseInt(content[1])))).queue();
                            } catch (NumberFormatException e) {
                                channel.sendMessageEmbeds(errorEmbed("could not parse argument \"number of runs to retrieve (`args[1]`)\" to integer (`NumberFormatException`)")).queue();
                            }
                        }
                    } else {
                        channel.sendMessageEmbeds(successEmbed(Main.getCounter(id).getPastRunsAsString(5))).queue();
                    }
                    break;
                case "best":
                    channel.sendMessageEmbeds(successEmbed(Main.getCounter(id).getBestRun().toString())).queue();
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
                .setAuthor(ID)
                .setColor(0x00FF00)
                .setDescription(description)
                .setFooter("made by ioxom")
                .build();
    }

    public static MessageEmbed errorEmbed(String description) {
        return new EmbedBuilder()
                .setAuthor(ID)
                .setColor(0xB40D0D)
                .setDescription(description)
                .setFooter("made by ioxom")
                .build();
    }
}
