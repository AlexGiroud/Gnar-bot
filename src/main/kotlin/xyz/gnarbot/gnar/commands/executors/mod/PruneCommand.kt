package xyz.gnarbot.gnar.commands.executors.mod

import net.dv8tion.jda.core.Permission
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import xyz.gnarbot.gnar.commands.Scope
import xyz.gnarbot.gnar.utils.Context
import xyz.gnarbot.gnar.utils.Utils
import xyz.gnarbot.gnar.utils.ln
import java.time.OffsetDateTime

@Command(
        aliases = arrayOf("prune", "delmessages", "delmsgs"),
        usage = "(amount)",
        description = "Delete up to 100 messages.",
        category = Category.MODERATION,
        scope = Scope.TEXT,
        permissions = arrayOf(Permission.MESSAGE_MANAGE)
)
class PruneCommand : CommandExecutor() {
    override fun execute(context: Context, args: Array<String>) {
        if (args.isEmpty()) {
            context.send().embed("Prune Messages") {
                description { info.description }
                field("Options") {
                    buildString {
                        append("`(amount)` • Delete that amount of messages (up to 100).").ln()
                    }
                }
            }.action().queue()
            return
        }

        context.message.delete().queue()

        val history = context.channel.history

        val amount = args[0].toIntOrNull()?.coerceIn(0, 100) ?: kotlin.run {
            context.send().error("Improper arguments supplies, must be a number.").queue()
            return
        }

        if (amount < 2) {
            context.send().error("You need to delete 2 or more messages to use this command.").queue()
            return
        }

        val time = OffsetDateTime.now().minusWeeks(2)

        history.retrievePast(amount).queue {
            val messages = it.filter { msg ->
                msg.creationTime.isAfter(time)
            }

            when {
                messages.size >= 2 -> {
                    context.message.textChannel.deleteMessages(messages).queue()
                    context.send().info("Attempted to delete **${messages.size}** messages.\nDeleting this message in **5** seconds.")
                            .queue(Utils.deleteMessage(5))
                }
                messages.size == 1 -> {
                    messages.first().delete().queue()
                    context.send().info("Attempted to delete **1** messages.\nDeleting this message in **5** seconds.")
                            .queue(Utils.deleteMessage(5))
                }
                else -> {
                    context.send().info("No messages were found (that are younger than 2 weeks).\nDeleting this message in **5** seconds.")
                            .queue(Utils.deleteMessage(5))
                }
            }
        }
    }
}
