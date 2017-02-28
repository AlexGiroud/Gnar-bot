package xyz.gnarbot.gnar.commands.executors.music.parent

import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import java.awt.Color

abstract class MusicExecutor : CommandExecutor() {

    val musicColor = Color(0, 221, 88)

//    init {
//        symbol = "**♬**"
//    }

    //abstract fun execute(note: Note, args: List<String>, host: Host, manager: MusicManager)

    protected fun getTimestamp(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt() % 60
        val minutes = (milliseconds / (1000 * 60) % 60).toInt()
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt()

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            return String.format("%02d:%02d", minutes, seconds)
        }
    }
}
