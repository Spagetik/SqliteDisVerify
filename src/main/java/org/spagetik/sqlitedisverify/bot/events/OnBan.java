package org.spagetik.sqlitedisverify.bot.events;

import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.spagetik.sqlitedisverify.SqliteDisVerify;

public class OnBan extends ListenerAdapter {

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        SqliteDisVerify.getGuildDb().banMember(event.getUser());
    }
}
