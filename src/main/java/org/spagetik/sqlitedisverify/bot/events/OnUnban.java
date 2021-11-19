package org.spagetik.sqlitedisverify.bot.events;

import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.spagetik.sqlitedisverify.SqliteDisVerify;

public class OnUnban extends ListenerAdapter {
    @Override
    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        SqliteDisVerify.getGuildDb().unBanMember(event.getUser());
    }
}
