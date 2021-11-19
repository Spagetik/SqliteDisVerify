package org.spagetik.sqlitedisverify.bot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.spagetik.sqlitedisverify.SqliteDisVerify;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class VerifySlashCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getName().equalsIgnoreCase("verify")) {
            String code = event.getOption("code").getAsString();
            Member member = event.getMember();
            Guild guild = event.getGuild();
            UUID uuid = SqliteDisVerify.getCodeDb().checkCode(code);
            if (uuid != null) {
                HashMap<String, String> memberExist = SqliteDisVerify.getGuildDb().checkIfMemberExist(uuid);
                if (memberExist == null) {
                    String nickname = getNickname(uuid);
                    addMemberToDatabase(member, uuid, nickname);
                    giveRole(member, guild);
                    try {
                        setNickname(member, nickname);
                    } catch (HierarchyException e) {
                        assert true;
                    }
                    sendMsgSuccess(event);
                }
                else {
                    if (Objects.equals(memberExist.get("2"), member.getId())) {
                        if (Objects.equals(memberExist.get("3"), "0")) {
                            String nickname = getNickname(uuid);
                            assert guild != null;
                            giveRole(member, guild);
                            try {
                                setNickname(member, nickname);
                            } catch (HierarchyException e) {
                                assert true;
                            }
                            sendMsgSuccess(event);
                        }
                        else {
                            sendBanMessage(event);
                        }
                    }
                    else {
                        if (Objects.equals(memberExist.get("3"), "0")) {
                            String oldMemberId = memberExist.get("2");
                            User oldUser = SqliteDisVerify.getJda().getUserById(oldMemberId);
                            if (oldUser != null) {
                                oldUser.openPrivateChannel()
                                        .flatMap(channel -> channel.sendMessage("<@" + oldMemberId + "> , you verified from new account - <@" + member.getId() + ">." +
                                                "\n\n**If you don't move your account, text to server administration!**"))
                                        .queue();
                                assert guild != null;
                                Member oldMember = guild.getMember(oldUser);
                                if (oldMember != null) {
                                    List<Role> roles = removeAllRoles(oldMember, guild);
                                    giveRoles(member, guild, roles);
                                }
                            }
                            else {
                                assert guild != null;
                                giveRole(member, guild);
                            }
                            String nickname = getNickname(uuid);
                            editDiscordId(oldMemberId, member.getId());
                            setNickname(member, nickname);
                            sendMsgSuccess(event);
                        }
                        else {
                            sendBanMessage(event);
                        }
                    }
                }
                SqliteDisVerify.getCodeDb().removeCodeFromDb(Integer.parseInt(code));
            }
            else {
                sendMsgCodeDoesntExist(event);
            }
        }
    }

    private String getNickname (UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    private void sendMsgCodeDoesntExist(@NotNull SlashCommandEvent event) {
        event.reply(SqliteDisVerify.getInstance().getConfig().getString("discord.messages.no_code")).setEphemeral(true).queue();
    }

    private void sendMsgSuccess(@NotNull SlashCommandEvent event) {
        event.reply(SqliteDisVerify.getInstance().getConfig().getString("discord.messages.successful")).setEphemeral(true).queue();
    }

    private void sendBanMessage(@NotNull SlashCommandEvent event) {
        event.reply(SqliteDisVerify.getInstance().getConfig().getString("discord.messages.banned")).setEphemeral(true).queue();
    }

    private void setNickname(@NotNull Member member, @NotNull String nickname) {
        try {
            member.modifyNickname(nickname).queue();
        }
        catch (Exception e) {
            SqliteDisVerify.getInstance().getLogger().info("Player " + nickname + " isnt in Discord Server or cant change nick");
        }
    }

    private void giveRole(@NotNull Member member, @NotNull Guild guild) {
        String role_id = SqliteDisVerify.getInstance().getConfig().getString("discord.guild.common_role_id");
        Role role = guild.getRoleById(role_id);
        guild.addRoleToMember(member, role).queue();
    }

    private void giveRoles(@NotNull Member member, @NotNull Guild guild, @NotNull List<Role> roles) {
        for (Role role : roles) {
            guild.addRoleToMember(member, role).queue();
        }
    }

    private @NotNull List<Role> removeAllRoles(@NotNull Member member, @NotNull Guild guild) {
        List<Role> roles = member.getRoles();
        for (Role role : roles) {
            guild.removeRoleFromMember(member, role).queue();
        }
        return roles;
    }

    private void addMemberToDatabase(@NotNull Member member, @NotNull UUID uuid, @NotNull String nickname) {
        SqliteDisVerify.getGuildDb().addNewMember(member, uuid, nickname);
    }

    private void editDiscordId(@NotNull String old_id, @NotNull String new_id) {
        SqliteDisVerify.getGuildDb().editDiscordId(old_id, new_id);
    }

}
