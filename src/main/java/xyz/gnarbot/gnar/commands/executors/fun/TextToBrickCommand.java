package xyz.gnarbot.gnar.commands.executors.fun;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Context;

@Command(
        aliases = "ttb",
        usage = "(string)",
        description = "Text to bricks fun."
)
public class TextToBrickCommand extends CommandExecutor {
    @Override
    public void execute(Context context, String[] args) {
        if (args.length == 0) {
            context.send().error("Please provide a query.").queue();
            return;
        }

        context.send().embed("Text to Brick")
                .setColor(context.getConfig().getAccentColor())
                .description(() -> {
                    StringBuilder sb = new StringBuilder();
                    for (String a : StringUtils.join(args, " ").split("")) {
                        if (Character.isLetter(a.toLowerCase().charAt(0))) {
                            sb.append(":regional_indicator_").append(a.toLowerCase()).append(":");
                        } else {
                            if (a.equals(" ")) {
                                sb.append(" ");
                            }
                            sb.append(a);
                        }
                    }
                    return sb.toString();
                })
                .action().queue();
    }
}
