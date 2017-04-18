package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.BotConfig
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import xyz.gnarbot.gnar.commands.Scope

@Command(
        aliases = arrayOf("stop"),
        description = "Stop and clear the music player.",
        category = Category.MUSIC,
        scope = Scope.VOICE,
        permissions = arrayOf(Permission.MANAGE_CHANNEL)
)
class StopCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val manager = guildData.musicManager

        manager.scheduler.queue.clear()
        manager.player.stopTrack()
        manager.player.isPaused = false
        guild.audioManager.closeAudioConnection()

        message.respond().embed("Stop Playback") {
            color = BotConfig.MUSIC_COLOR
            description = "Playback has been completely stopped and the queue has been cleared."
        }.rest().queue()
    }
}
