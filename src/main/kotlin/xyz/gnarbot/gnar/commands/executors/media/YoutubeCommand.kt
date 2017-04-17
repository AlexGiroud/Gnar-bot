package xyz.gnarbot.gnar.commands.executors.media

import net.dv8tion.jda.core.b
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.link
import org.json.JSONException
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.YouTube
import java.awt.Color

@Command(
        aliases = arrayOf("youtube"),
        usage = "-query...",
        description = "Search and get a YouTube video."
)
class YoutubeCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        if (args.isEmpty()) {
            message.respond().error("Gotta put something to search YouTube.").queue()
            return
        }

        try {
            val query = args.joinToString("+")

            val results = YouTube.search(query, 3)

            if (results.isEmpty()) {
                message.respond().error("No search results for `$query`.").queue()
                return
            }
            var firstUrl: String? = null

            message.respond().embed {
                setAuthor("YouTube Results", "https://www.youtube.com", "https://s.ytimg.com/yts/img/favicon_144-vflWmzoXw.png")
                thumbnail = "https://gnarbot.xyz/assets/img/youtube.png"
                color = Color(141, 20, 0)

                description {
                    buildString {
                        for (result in results) {

                            val title = result.title
                            val desc = result.description
                            val url = result.url

                            if (firstUrl == null) {
                                firstUrl = url
                            }

                            appendln(b(title link url)).appendln(desc)
                        }
                    }
                }
            }.rest().queue()

            message.respond().text("**First Video:** $firstUrl").queue()
        } catch (e: JSONException) {
            message.respond().error("Unable to get YouTube results.").queue()
            e.printStackTrace()
        } catch (e: NullPointerException) {
            message.respond().error("Unable to get YouTube results.").queue()
            e.printStackTrace()
        }
    }
}



