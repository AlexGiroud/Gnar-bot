package xyz.gnarbot.gnar.listeners;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.Credentials;
import xyz.gnarbot.gnar.Shard;

public class GuildCountListener extends ListenerAdapter {
    private final Bot bot;

    private int changes = 0;

    public GuildCountListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        updateQueue();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        updateQueue();
    }

    private void updateQueue() {
        changes++;

        if (changes > 20) {
            update();
            changes = 0;
        }
    }

    /**
     * Updates Server Counts on ad sites
     */
    private void update() {
        int count = 0;

        for (Shard shard : bot.getShards()) {
            count += shard.getGuilds().size();
        }

        updateAbalCount(count);
        updateCarbonitexCount(count);
        updateDiscordBotsCount(count);
    }

    private void updateAbalCount(int i) {
        try {
            String auth = Credentials.ABAL;

            JSONObject json = new JSONObject().put("server_count", i);

            String response = Unirest.post("https://bots.discord.pw/api/bots/201503408652419073/stats")
                    .header("User-Agent", "Gnar Bot")
                    .header("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(json)
                    .asString()
                    .getStatusText();

            bot.getLog().info("Successfully updated Abal server count to " + i + ".");
            bot.getLog().info("Response code: " + response);
        } catch (UnirestException e) {
            bot.getLog().warn("Failed updating Abal server count to " + i + ".");
            e.printStackTrace();
        }
    }

    private void updateDiscordBotsCount(int i) {
        try {
            String auth = Credentials.DISCORDBOTS;

            JSONObject json = new JSONObject().put("server_count", i);

            String response = Unirest.post("https://discordbots.org/api/bots/201503408652419073/stats")
                    .header("User-Agent", "Gnar Bot")
                    .header("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(json)
                    .asString()
                    .getStatusText();

            bot.getLog().info("Successfully updated DiscordBots server count to " + i + ".");
            bot.getLog().info("Response code: " + response);
        } catch (UnirestException e) {
            bot.getLog().warn("Failed updating DiscordBots server count to " + i + ".");
            e.printStackTrace();
        }
    }

    private void updateCarbonitexCount(int i) {
        try {
            String auth = Credentials.ABAL;
            String key = Credentials.CARBONITEX;

            JSONObject json = new JSONObject().put("key", key).put("servercount", i);

            String response = Unirest.post("https://www.carbonitex.net/discord/data/botdata.php")
                    .header("User-Agent", "Gnar Bot")
                    .header("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(json)
                    .asString()
                    .getStatusText();

            bot.getLog().info("Successfully updated Carbonitex server count to " + i + ".");
            bot.getLog().info("Response code: " + response);
        } catch (UnirestException e) {
            bot.getLog().warn("Failed updating Carbonitex server count to " + i + ".");
            e.printStackTrace();
        }
    }
}
