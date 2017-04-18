package xyz.gnarbot.gnar

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import xyz.gnarbot.gnar.api.data.ShardInfo
import xyz.gnarbot.gnar.guilds.GuildData
import xyz.gnarbot.gnar.listeners.ShardListener

/**
 * Individual shard instances of [JDA] of the bot that contains all the [GuildData] for each guild.
 */
class Shard(val id: Int, private val jda: JDA, val bot: Bot) : JDA by jda {
    val guildData = mutableMapOf<Long, GuildData>()

    init {
        jda.addEventListener(ShardListener(this, bot))
    }

    fun getGuildData(id: Long) : GuildData {
        return guildData.getOrPut(id) { GuildData(id, this, bot) }
    }

    /**
     * Lazily get a Host instance from a Guild instance.
     *
     * @param guild JDA Guild.
     *
     * @return Host instance of Guild.
     *
     * @see GuildData
     */
    fun getGuildData(guild: Guild) = getGuildData(guild.idLong)

    /**
     * @return The string representation of the shard.
     */
    override fun toString(): String {
        return "Shard(id=$id, guilds=${jda.guilds.size})"
    }

    /**
     * @return JSON data on the shard.
     */
    val info: ShardInfo get() = ShardInfo(this)

    /**
     * Shuts down the shard.
     */
    override fun shutdown() {
        jda.shutdown(false)
        clearData(true)
    }

    fun clearData(interrupt: Boolean) {
        val iterator = guildData.iterator()
        while (iterator.hasNext()) {
            val it = iterator.next()
            if(it.value.reset(interrupt)) {
                iterator.remove()
            }
        }
    }
}
