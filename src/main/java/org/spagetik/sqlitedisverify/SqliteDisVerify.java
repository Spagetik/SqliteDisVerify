package org.spagetik.sqlitedisverify;

import net.dv8tion.jda.api.JDA;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spagetik.sqlitedisverify.backgroundProcesses.CheckingUsernames;
import org.spagetik.sqlitedisverify.bot.DiscordBot;
import org.spagetik.sqlitedisverify.commands.BanDiscordCommand;
import org.spagetik.sqlitedisverify.commands.DiscordVerifyCommand;
import org.spagetik.sqlitedisverify.commands.UnbanDiscordCommand;
import org.spagetik.sqlitedisverify.sql.CodeDb;
import org.spagetik.sqlitedisverify.sql.GuildDb;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class SqliteDisVerify extends JavaPlugin {

    private static SqliteDisVerify instance;
    private static CodeDb codeDb;
    private static GuildDb guildDb;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        new DiscordBot();
        new DiscordVerifyCommand();
        new BanDiscordCommand();
        new UnbanDiscordCommand();
        File dataFolder = new File(getDataFolder(), "DiscordVerify.db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                this.getLogger().log(Level.SEVERE, "File write error: DiscordVerify.db");
            }
        }
        codeDb = new CodeDb(getDataFolder().getPath(), "DiscordVerify");
        guildDb = new GuildDb(getDataFolder().getPath(), "DiscordVerify");
        new Thread(() -> Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            this.getLogger().info("Started checking nick");
            CheckingUsernames.check();
            this.getLogger().info("Finished checking nick");
        }, 1000, 1500000)).start();
    }

    @Override
    public void onDisable() {
        getJda().shutdown();
    }

    public static SqliteDisVerify getInstance() {
        return instance;
    }

    public static JDA getJda() {
        return DiscordBot.getJda();
    }

    public static CodeDb getCodeDb() {
        return codeDb;
    }

    public static GuildDb getGuildDb() {
        return guildDb;
    }
}
