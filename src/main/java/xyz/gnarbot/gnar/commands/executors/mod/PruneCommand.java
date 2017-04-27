package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;
import xyz.gnarbot.gnar.commands.Scope;
import xyz.gnarbot.gnar.utils.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(
        aliases = {"prune", "delmessages", "delmsgs"},
        usage = "-amount -words...",
        description = "Delete up to 100 messages.",
        category = Category.MODERATION,
        scope = Scope.TEXT,
        permissions = Permission.MESSAGE_MANAGE
)
public class PruneCommand extends CommandExecutor {

    @Override
    public void execute(Context context, String[] args) {
        if (args.length == 0) {
            context.send().error("Insufficient amount of arguments.").queue();
            return;
        }
        
        context.getMessage().delete().queue();

        MessageHistory history = context.send().getChannel().getHistory();

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
            amount = Math.min(amount, 100);
        } catch (NumberFormatException e) {
            context.send().error("Improper arguments supplies, must be a number.").queue();
            return;
        }

        if (amount < 2) {
            context.send().error("You need to delete 2 or more messages to use this command.").queue();
            return;
        }

        history.retrievePast(amount).queue(msgs -> {
            if (args.length >= 2) {
                String[] filter = Arrays.copyOfRange(args, 1, args.length);

                List<Message> _msgs = new ArrayList<>();

                for (Message msg : msgs) {
                    for (String word : filter) {
                        if (msg.getContent().contains(word)) {
                            _msgs.add(msg);
                        }
                    }
                }
                msgs = _msgs;
            }

            context.getMessage().getTextChannel().deleteMessages(msgs).queue();

            context.send().info("Attempted to delete **[" + msgs.size() + "]()** messages.\nDeleting this message in **5** seconds.")
                    .queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
        });
    }
}
