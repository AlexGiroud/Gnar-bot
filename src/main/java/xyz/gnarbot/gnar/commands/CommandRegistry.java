package xyz.gnarbot.gnar.commands;

import xyz.gnarbot.gnar.commands.executors.admin.GarbageCollectCommand;
import xyz.gnarbot.gnar.commands.executors.admin.JavascriptCommand;
import xyz.gnarbot.gnar.commands.executors.admin.RestartShardsCommand;
import xyz.gnarbot.gnar.commands.executors.admin.ThrowError;
import xyz.gnarbot.gnar.commands.executors.fun.*;
import xyz.gnarbot.gnar.commands.executors.games.GameLookupCommand;
import xyz.gnarbot.gnar.commands.executors.games.LeagueLookupCommand;
import xyz.gnarbot.gnar.commands.executors.games.OverwatchLookupCommand;
import xyz.gnarbot.gnar.commands.executors.general.*;
import xyz.gnarbot.gnar.commands.executors.media.*;
import xyz.gnarbot.gnar.commands.executors.mod.*;
import xyz.gnarbot.gnar.commands.executors.music.NowPlayingCommand;
import xyz.gnarbot.gnar.commands.executors.music.PlayCommand;
import xyz.gnarbot.gnar.commands.executors.music.QueueCommand;
import xyz.gnarbot.gnar.commands.executors.music.VoteSkipCommand;
import xyz.gnarbot.gnar.commands.executors.music.dj.*;
import xyz.gnarbot.gnar.commands.executors.polls.PollCommand;
import xyz.gnarbot.gnar.commands.executors.test.TestCommand;
import xyz.gnarbot.gnar.commands.executors.test.TestEmbedCommand;
import xyz.gnarbot.gnar.textadventure.AdventureCommand;
import xyz.gnarbot.gnar.textadventure.StartAdventureCommand;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A registry storing CommandExecutor entries for the bot.
 */
public class CommandRegistry {
    /** The mapped registry of invoking key to the classes. */
    private final Map<String, CommandEntry> commandEntryMap = new LinkedHashMap<>();

    public CommandRegistry() {
        register(HelpCommand.class);
        register(InviteBotCommand.class);
        register(PingCommand.class);
        register(MathCommand.class);
        register(RemindMeCommand.class);
        register(GoogleCommand.class);
        register(YoutubeCommand.class);
        register(UrbanDictionaryCommand.class);
        register(UptimeCommand.class);
        register(WhoIsCommand.class);
        register(BotInfoCommand.class);
        register(DonateCommand.class);
        //End General Commands

        //Fun Commands
        register(ASCIICommand.class);
        register(CoinFlipCommand.class);
        register(DialogCommand.class);
        register(YodaTalkCommand.class);
        register(RollCommand.class);
        register(PoopCommand.class);
        register(GoodShitCommand.class);
        register(EightBallCommand.class);
        register(LeetifyCommand.class);
        register(MarvelComics.class);
        //register(ProgressionCommand.class);
        register(GoogleyEyesCommand.class);
        //register(ServersSharedCommand.class);
        register(TextToSpeechCommand.class);
        register(ReactCommand.class);
        register(ChampDataCommand.class);
        register(TriviaAnswerCommand.class);
        register(TriviaCommand.class);
        //register(GraphCommand.class);

        register(ChampQuoteCommand.class);
        register(PandoraBotCommand.class);
        register(MemeCommand.class);
        //End Fun Commands

        //Mod Commands
        register(BanCommand.class);
        register(KickCommand.class);
        register(UnbanCommand.class);
        register(PruneCommand.class);
        register(DeleteMessageCommand.class);
        //End Mod Commands

        //Testing Commands
        register(TestCommand.class);
        //End Testing Commands

        //Text Adventure Commands
        register(AdventureCommand.class);
        register(StartAdventureCommand.class);
        //End Text Adventure Commands

        //Game Commands
        register(OverwatchLookupCommand.class);
        register(LeagueLookupCommand.class);
        register(GameLookupCommand.class);
        //End Game Commands

        //Poll Commands
        register(PollCommand.class);
        //End Poll Commands

        //Media Commands
        register(CatsCommand.class);
        register(ExplosmCommand.class);
        register(ExplosmRCGCommand.class);
        //register(GarfieldCommand.class);
        register(XKCDCommand.class);
        //End Media Commands

        // Administrator commands
        register(GarbageCollectCommand.class);
        register(RestartShardsCommand.class);
        register(JavascriptCommand.class);
        register(ShardInfoCommand.class);
        register(ThrowError.class);


        // Test Commands
        register(TestEmbedCommand.class);
        register(QuoteCommand.class);
        register(TextToBrickCommand.class);

        //MUSIC COMMAND
        register(PlayCommand.class);
        register(LeaveCommand.class);
        register(PauseCommand.class);
        register(StopCommand.class);
        register(SkipCommand.class);
        register(ShuffleCommand.class);
        register(NowPlayingCommand.class);
        register(QueueCommand.class);
        register(RestartCommand.class);
        register(RepeatCommand.class);
        register(ResetCommand.class);
        register(VoteSkipCommand.class);

        register(EnableCommand.class);
        register(DisableCommand.class);
        register(ListDisabledCommand.class);

//        register(new JSCommandExecutor(new File("data/scripting/javascript/firstJSCommand.js")));
    }

    public Map<String, CommandEntry> getCommandEntriesMap() {
        return commandEntryMap;
    }

    public void register(Class<? extends CommandExecutor> cls) {
        if (!cls.isAnnotationPresent(Command.class)) {
            throw new IllegalStateException("@Command annotation not found for class: " + cls.getName());
        }

        CommandEntry entry = new CommandEntry(cls);
        for (String alias : entry.info.aliases()) {
            registerCommand(alias, entry);
        }
    }

    /**
     * Register the CommandExecutor instance into the registry.
     * @param label Invoking key.
     * @param entry CommandEntry.
     */
    public void registerCommand(String label, CommandEntry entry) {
        label = label.toLowerCase();
        if (commandEntryMap.containsKey(label)) {
            throw new IllegalStateException("Command " + label + " is already registered.");
        }
        commandEntryMap.put(label, entry);
    }

    /**
     * Unregisters a CommandExecutor.
     *
     * @param label Invoking key.
     */
    public void unregisterCommand(String label) {
        commandEntryMap.remove(label);
    }

    /**
     * Returns the command registry.
     *
     * @return The command registry.
     */
    public Set<CommandEntry> getEntries() {
        return new LinkedHashSet<>(commandEntryMap.values());
    }

    /**
     * Get the command based on the key.
     *
     * @param key Invoking key.
     * @return CommandExecutor instance.
     */
    public CommandEntry getEntry(String key) {
        return commandEntryMap.get(key);
    }

    public static class CommandEntry {
        public final Class<? extends CommandExecutor> cls;
        public final Command info;

        CommandEntry(Class<? extends CommandExecutor> cls) {
            this.cls = cls;
            this.info = cls.getAnnotation(Command.class);
        }
    }
}
