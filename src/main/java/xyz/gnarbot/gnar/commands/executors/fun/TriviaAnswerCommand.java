package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.TriviaQuestions;

@Command(
        aliases = "answer",
        category = Category.FUN
)
public class TriviaAnswerCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        if (TriviaQuestions.isNotSetup()) {
            TriviaQuestions.init();
        }

        try {
            message.respond().info(TriviaQuestions.getAnswer(Integer.valueOf(args[0]))).queue();
        } catch (Exception e) {
            message.respond().error("Please enter a number.").queue();
        }
    }

}
