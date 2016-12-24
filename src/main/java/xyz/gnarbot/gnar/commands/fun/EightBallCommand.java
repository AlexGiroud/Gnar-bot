package xyz.gnarbot.gnar.commands.fun;

import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Random;

@Command(aliases = "8ball", usage = "(question)", description = "Test your wildest dreams!")
public class EightBallCommand extends CommandExecutor
{
    private final Random random = new Random();
    
    private final String[] responses = {"It is certain", "It is decidedly so", "Without a doubt", "Yes, definitely",
            "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes",
            "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate " +
            "and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very" +
            " doubtful"};
    
    @Override
    public void execute(Note message, String label, String[] args)
    {
        if (args.length == 0)
        {
            message.getChannel().sendMessage(String.format("%s ➜ At ask 8-ball something. `=[`", message.getAuthor()
                    .getAsMention()));
            return;
        }
        
        message.reply("`" + responses[random.nextInt(responses.length)] + "`.");
    }
}
