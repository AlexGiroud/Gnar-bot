package xyz.gnarbot.gnar.listeners;


import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ExceptionEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.BotConfiguration;
import xyz.gnarbot.gnar.Shard;
import xyz.gnarbot.gnar.guilds.GuildData;
import xyz.gnarbot.gnar.utils.Context;

public class ShardListener extends ListenerAdapter {
    private final Shard shard;
    private final Bot bot;

    public ShardListener(Shard shard, Bot bot) {
        this.shard = shard;
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            if (event.getMessage().getContent().startsWith(BotConfiguration.PREFIX)) {
                GuildData gd = shard.getGuildData(event.getGuild());

                gd.handleCommand(new Context(event.getMessage(), event.getGuild(), gd, shard, bot));
            }
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        shard.getGuildData().remove(event.getGuild().getIdLong());
    }

    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent event) {
        if (event instanceof GuildVoiceLeaveEvent || event instanceof GuildVoiceMoveEvent) {
            if (event.getMember().getUser() == event.getJDA().getSelfUser()) return;

            Guild guild = event.getGuild();

            if (guild == null) return;

            VoiceChannel botChannel = guild.getSelfMember().getVoiceState().getChannel();

            VoiceChannel channelLeft;

            if (event instanceof GuildVoiceLeaveEvent) {
                channelLeft = ((GuildVoiceLeaveEvent) event).getChannelLeft();
            } else {
                channelLeft = ((GuildVoiceMoveEvent) event).getChannelLeft();
            }

            if (botChannel == null || channelLeft != botChannel) return;

            if (botChannel.getMembers().size() == 1) {
                GuildData data = shard.getGuildData(event.getGuild());
                data.resetMusicManager();
            }
        }
    }

//    @Override
//    public void onMessageReactionAdd(MessageReactionAddEvent event) {
//
//        long msg_id = event.getMessageIdLong();
//        if (event.getMember().equals(event.getGuild().getSelfMember())) return;
//        event.getChannel().getMessageById(msg_id).queue((msg) -> {
//            if (!msg.getAuthor().equals(shard.getSelfUser())) return;
//            if (msg.getEmbeds().isEmpty()) return;
//            MessageEmbed embed = msg.getEmbeds().get(0);
//            if (!embed.getTitle().equals("Poll")) return;
//
//            User author = event.getMember();
//            for (MessageReaction reaction : msg.getReactions()) {
//                if (reaction.equals(event.getReaction())) continue;
//                event.getReaction().removeReaction(author).queue();
//            }
//        });
//    }

    @Override
    public void onResume(ResumedEvent event) {
        bot.getLog().info("JDA " + shard.getId() + " has resumed.");
    }

    @Override
    public void onReconnect(ReconnectedEvent event) {
        bot.getLog().info("JDA " + shard.getId() + " has reconnected.");
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        if (event.isClosedByServer()) {
            bot.getLog().info("JDA " + shard.getId() + " has disconnected. Code: "
                    + event.getServiceCloseFrame().getCloseCode() + event.getCloseCode());
        } else {
            bot.getLog().info("JDA " + shard.getId() + " has disconnected. Code: "
                    + event.getClientCloseFrame().getCloseCode() + " " + event.getClientCloseFrame().getCloseReason());
        }
    }

    @Override
    public void onException(ExceptionEvent event) {
        if (!event.isLogged()) bot.getLog().error("Error thrown by JDA.", event.getCause());
    }
}
