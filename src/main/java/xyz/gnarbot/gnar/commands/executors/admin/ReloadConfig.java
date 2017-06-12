package xyz.gnarbot.gnar.commands.executors.admin;

import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Context;

@Command(
        aliases = "reloadConfig",
        admin = true,
        category = Category.NONE
)
public class ReloadConfig extends CommandExecutor {
    @Override
    public void execute(Context context, String[] args) {
        context.getConfig().reload();
    }
}
