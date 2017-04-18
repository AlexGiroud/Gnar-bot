package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.BotConfig
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("remindme", "remind"), usage = "(duration) (time unit) (msg)")
class RemindMeCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        if (args.size >= 3) {
            val string = args.copyOfRange(2, args.size).joinToString(" ")

            val time = args[0].toIntOrNull() ?: kotlin.run {
                message.respond().error("The time number was not an integer.").queue()
                return
            }

            val timeUnit = try {
                TimeUnit.valueOf(args[1].toUpperCase())
            } catch (e: IllegalArgumentException) {
                message.respond().error("The specified time unit was invalid. \n`${TimeUnit.values().contentToString()}`").queue()
                return
            }

            if (time > 0) {
                message.respond().embed("Reminder Scheduled") {
                    color = BotConfig.COLOR
                    description = "I'll be reminding you in __$time ${timeUnit.toString().toLowerCase()}__."
                }.rest().queue()

                message.author.openPrivateChannel().queue {
                    it.send().embed("Reminder from $time ${timeUnit.toString().toLowerCase()} ago.") {
                        color = BotConfig.COLOR
                        description = string
                    }.rest().queueAfter(time.toLong(), timeUnit)
                }
            } else {
                message.respond().error("Number must be more than 0.").queue()
            }
        } else {
            message.respond().error("Insufficient amount of arguments. `(#) (unit) (msg)`").queue()
        }
    }
}
