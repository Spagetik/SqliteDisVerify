package org.spagetik.sqlitedisverify.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.spagetik.sqlitedisverify.SqliteDisVerify;
import org.spagetik.sqlitedisverify.bot.commands.VerifySlashCommand;
import org.spagetik.sqlitedisverify.bot.events.OnBan;
import org.spagetik.sqlitedisverify.bot.events.OnReady;
import org.spagetik.sqlitedisverify.bot.events.OnUnban;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class DiscordBot {

    private static JDA jda;

    public DiscordBot() {
        EnumSet<GatewayIntent> intents = EnumSet.allOf(
                GatewayIntent.class
        );
        try {
            String token = SqliteDisVerify.getInstance().getConfig().getString("discord.bot.token");
            jda = JDABuilder.create(token, intents).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        jda.addEventListener(new OnReady());
        jda.addEventListener(new VerifySlashCommand());
        jda.addEventListener(new OnBan());
        jda.addEventListener(new OnUnban());
    }

    public static JDA getJda() {
        return jda;
    }

}
