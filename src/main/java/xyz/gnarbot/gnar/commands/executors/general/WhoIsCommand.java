package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Context;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Command(
        id = 22,
        aliases = {"user", "whois", "who"},
        usage = "[user]",
        description = "Get information on a user."
)
public class WhoIsCommand extends CommandExecutor {
    @Override
    public void execute(Context context, String label, String[] args) {
        final Member member;

        if (args.length == 0) {
            member = context.getMember();
        } else {
            List<User> mentioned = context.getMessage().getMentionedUsers();
            if (!mentioned.isEmpty()) {
                member = context.getGuild().getMember(mentioned.get(0));
            } else {
                List<Member> list = context.getGuild().getMembersByName(StringUtils.join(args, " "), false);
                if (list.isEmpty()) {
                    list = context.getGuild().getMembersByNickname(StringUtils.join(args, " "), false);
                    if (list.isEmpty()) {
                        context.send().error("You did not mention a valid user.").queue();
                        return;
                    }
                }
                member = list.get(0);
            }
        }

        if (member == null) {
            context.send().error("You did not mention a valid user.").queue();
            return;
        }

        StringBuilder roleStr = new StringBuilder();
        for (Role role : member.getRoles()) {
            roleStr.append(role.getName()).append(' ');
        }

        context.send().embed("Who is " + member.getEffectiveName() + "?")
                .setColor(member.getColor())
                .setThumbnail(member.getUser().getEffectiveAvatarUrl())

                .field("Name", true, member.getUser().getName())
                .field("Discriminator", true, member.getUser().getDiscriminator())

                .field("ID", true, member.getUser().getId())
                .field("Status", true, StringUtils.capitalize(member.getOnlineStatus().getKey()))

                .field("Account Created", true, member.getUser().getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .field("Join Date", true, member.getJoinDate().format(DateTimeFormatter.ISO_LOCAL_DATE))

                .field("Nickname", true, member.getNickname() != null ? member.getNickname() : "No nickname.")
                .field("Game", true, member.getGame() != null ? member.getGame().getName() : "No game.")

                .field("Roles", true, roleStr)

                .action().queue();
    }
}
