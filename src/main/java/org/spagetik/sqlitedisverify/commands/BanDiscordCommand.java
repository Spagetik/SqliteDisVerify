package org.spagetik.sqlitedisverify.commands;

import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.spagetik.sqlitedisverify.SqliteDisVerify;

import java.util.HashMap;
import java.util.UUID;

public class BanDiscordCommand extends AbstractCommand {

    public BanDiscordCommand() {
        super("bandiscord");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("disban.permission")) {
            sender.sendMessage(ChatColor.RED + "No permission for this command");
            return;
        }
        String username = args[0];
        UUID uuid = Bukkit.getOfflinePlayer(username).getUniqueId();
        HashMap<String, String> memberExist = SqliteDisVerify.getGuildDb().checkIfMemberExist(uuid);
        if (memberExist != null) {
            String discordId = memberExist.get("2");
            Guild guild = SqliteDisVerify.getJda().getGuildById(SqliteDisVerify.getInstance().getConfig().getString("discord.guild.guild_id"));
            SqliteDisVerify.getGuildDb().banMember(discordId);
            guild.ban(discordId, 0, "Banned by server operator").queue();
        }
        sender.sendMessage("§2Player successfully banned in Discord!§f");
    }
}
