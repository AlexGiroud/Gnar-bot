package xyz.gnarbot.gnar.commands.executors.music.search

import com.jagrosh.jdautilities.menu.SelectorBuilder
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.utils.Utils
import xyz.gnarbot.gnar.utils.b
import xyz.gnarbot.gnar.utils.link
import xyz.gnarbot.gnar.utils.ln

@xyz.gnarbot.gnar.commands.Command(
        aliases = arrayOf("soundcloud", "sc"),
        usage = "(query...)",
        description = "Search and see YouTube results.",
        donor = true,
        scope = xyz.gnarbot.gnar.commands.Scope.TEXT,
        category = xyz.gnarbot.gnar.commands.Category.MUSIC
)
class SoundcloudCommand : xyz.gnarbot.gnar.commands.CommandExecutor() {
    override fun execute(context: xyz.gnarbot.gnar.utils.Context, args: Array<String>) {
        if (args.isEmpty()) {
            context.send().error("Input a query to search Soundcloud.").queue()
            return
        }

        val query = args.joinToString(" ")

        xyz.gnarbot.gnar.music.MusicManager.Companion.search("scsearch:$query", 5) { results ->
            if (results.isEmpty()) {
                context.send().error("No search results for `$query`.").queue()
                return@search
            }

            val manager = context.guildData.musicManager

            val botChannel = context.guild.selfMember.voiceState.channel
            val userChannel = context.member.voiceState.channel

            if (!Bot.CONFIG.musicEnabled || userChannel == null || botChannel != null && botChannel != userChannel) {
                context.send().embed {
                    setAuthor("SoundCloud Results", "https://soundcloud.com", "https://soundcloud.com/favicon.ico")
                    setThumbnail("https://gnarbot.xyz/assets/img/soundcloud.png")
                    setColor(java.awt.Color(255, 110, 0))

                    description {
                        buildString {
                            for (result in results) {

                                val title = result.info.title
                                val url = result.info.uri
                                val length = Utils.getTimestamp(result.duration)
                                val author = result.info.author

                                append(xyz.gnarbot.gnar.utils.b(title link url)).ln()
                                append("**`").append(length).append("`** by **").append(author).append("**").ln()
                            }
                        }
                    }

                    setFooter("Want to play one of these music tracks? Join a voice channel and reenter this command.", null)
                }.action().queue()
                return@search
            } else {
                SelectorBuilder(Bot.getWaiter()).apply {
                    setTitle("SoundCloud Results")
                    setDescription("Select one of the following options to play them in your current music channel.")
                    setColor(java.awt.Color(255, 110, 0))

                    setUser(context.user)

                    for (result in results) {
                        addOption("`${Utils.getTimestamp(result.info.length)}` ${b(result.info.title link result.info.uri)}") {
                            if (context.member.voiceState.inVoiceChannel()) {
                                manager.loadAndPlay(context, result.info.uri)
                            } else {
                                context.send().error("You're not in a voice channel anymore!").queue()
                            }
                        }
                    }
                }.build().display(context.channel)
            }
        }
    }
}



