package xyz.gnarbot.gnar.commands.executors.media

import net.dv8tion.jda.core.b
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.link
import org.jsoup.Jsoup
import xyz.gnarbot.gnar.BotConfig
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Command(
        aliases = arrayOf("google"),
        usage = "-query...",
        description = "Who needs browsers!?"
)
class GoogleCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        if (args.isEmpty()) {
            message.respond().error("Gotta have something to search Google.").queue()
            return
        }

        try {
            val query = args.joinToString(" ")

            val blocks = Jsoup.connect("http://www.google.com/search?q=${URLEncoder.encode(query, StandardCharsets.UTF_8.displayName())}")
                    .userAgent("Gnar")
                    .get()
                    .select(".g")

            if (blocks.isEmpty()) {
                message.respond().error("No search results for `$query`.").queue()
                return
            }

            message.respond().embed {
                color = BotConfig.COLOR
                setAuthor("Google Results", "https://www.google.com/", "https://www.google.com/favicon.ico")
                thumbnail = "https://gnarbot.xyz/assets/img/google.png"

                description {
                    var count = 0

                    buildString {
                        for (block in blocks) {
                            if (count >= 3) break

                            val list = block.select(".r>a")
                            if (list.isEmpty()) break

                            val entry = list[0]
                            val title = entry.text()
                            val url = entry.absUrl("href").replace(")", "\\)")
                            var desc: String? = null

                            val st = block.select(".st")
                            if (!st.isEmpty()) desc = st[0].text()

                            appendln(b(title link url)).appendln(desc)
                            count++
                        }
                    }
                }
            }.rest().queue()
        } catch (e: IOException) {
            message.respond().error("Caught an exception while trying to Google stuff.").queue()
            e.printStackTrace()
        }
    }
}
