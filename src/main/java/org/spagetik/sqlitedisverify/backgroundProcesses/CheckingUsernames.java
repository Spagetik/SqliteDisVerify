package org.spagetik.sqlitedisverify.backgroundProcesses;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.spagetik.sqlitedisverify.SqliteDisVerify;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CheckingUsernames {

    private static String getNickname(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    private static void setNickname(@NotNull Member member, @NotNull String nickname) {
        try {
            member.modifyNickname(nickname).queue();
        }
        catch (Exception e) {
            SqliteDisVerify.getInstance().getLogger().info("Player " + nickname + " isnt in Discord Server or cant change nick");
        }
    }

    private static void editDatabaseNickname(UUID uuid, String nickname) {
        SqliteDisVerify.getGuildDb().editNickname(uuid, nickname);
    }

    public static void check() {
        List<HashMap<String, String>> allPlayers = SqliteDisVerify.getGuildDb().getAll();
        for (HashMap<String, String> player: allPlayers) {
            UUID uuid = UUID.fromString(player.get("0"));
            String nickname = player.get("1");
            String discordId = player.get("2");
            String banned = player.get("3");
            if (banned.equalsIgnoreCase("1")) continue;
            String guildId = SqliteDisVerify.getInstance().getConfig().getString("discord.guild.guild_id");
            Guild guild = SqliteDisVerify.getJda().getGuildById(guildId);
            Member member = guild.getMemberById(discordId);
            if (!nickname.equalsIgnoreCase(getNickname(uuid))) {
                setNickname(member, nickname);
                editDatabaseNickname(uuid, nickname);
            }
            else if (!nickname.equalsIgnoreCase(member.getNickname())) {
                setNickname(member, nickname);
            }
        }
    }
}
