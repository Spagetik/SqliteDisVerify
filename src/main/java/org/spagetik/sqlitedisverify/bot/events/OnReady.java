package org.spagetik.sqlitedisverify.bot.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.spagetik.sqlitedisverify.SqliteDisVerify;

public class OnReady extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        SqliteDisVerify.getInstance().getLogger().info("Discord bot is ready");
        String guild_id = SqliteDisVerify.getInstance().getConfig().getString("discord.guild.guild_id");
        Guild guild = SqliteDisVerify.getJda().getGuildById(guild_id);
        CommandData verifyCommand = new CommandData("verify","Verify command")
                .addOption(OptionType.INTEGER, "code", "Code which you got from minecraft server", true);
        guild.upsertCommand(verifyCommand).queue();
    }
}
