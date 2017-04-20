package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;
import xyz.gnarbot.gnar.commands.Scope;

@Command(aliases = "ban",
        category = Category.MODERATION,
        scope = Scope.TEXT,
        permissions = Permission.BAN_MEMBERS)
public class BanCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        Member author = message.getMember();
        Member target = null;

        if (message.getMentionedChannels().size() >= 1) {
            target = getGuild().getMember(message.getMentionedUsers().get(0));
        } else if (args.length >= 1) {
            target = getGuildData().getMemberByName(args[0], false);
        }

        if (target == null) {
            message.send().error("Could not find user.").queue();
            return;
        }
        if (!author.canInteract(target)) {
            message.send().error("Sorry, that user has an equal or higher role.").queue();
            return;
        }

        getGuild().getController().ban(target, 2).queue();
        message.send().info(target.getEffectiveName() + " has been banned.").queue();
    }
}