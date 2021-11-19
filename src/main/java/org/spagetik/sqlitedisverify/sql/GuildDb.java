package org.spagetik.sqlitedisverify.sql;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GuildDb extends AbstaractDb{

    public GuildDb(String path, String name) {
        super(path, name);
        this.execute("CREATE TABLE IF NOT EXISTS discord_users (" +
                "uuid VARCHAR(36) NOT NULL, " +
                "username VARCHAR(16) NOT NULL," +
                "discord_id BIGINT NOT NULL," +
                "banned BOOL NOT NULL)");
    }

    public HashMap<String, String> checkIfMemberExist(@NotNull UUID uuid) {
        String[] data = new String[1];
        data[0] = String.valueOf(uuid);
        try {
            return executeSelect("SELECT * FROM discord_users WHERE uuid = ?", data, 4).get(0);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public List<HashMap<String, String>> getAll() {
        return executeSelect("SELECT * FROM discord_users", 4);
    }

    public void addNewMember(@NotNull Member member, @NotNull UUID uuid, @NotNull String nickname) {
        String[] data = new String[4];
        data[0] = String.valueOf(uuid);
        data[1] = nickname;
        data[2] = member.getId();
        data[3] = "0";
        this.execute("INSERT INTO discord_users (uuid, username, discord_id, banned) VALUES (?, ?, ?, ?)", data);
    }

    public void editNickname(UUID uuid, String nickname) {
        String[] data = new String[2];
        data[0] = nickname;
        data[1] = String.valueOf(uuid);
        execute("UPDATE discord_users SET username = ? WHERE uuid = ?", data);
    }

    public void editDiscordId(String old_id, String new_id) {
        String[] data = new String[2];
        data[0] = new_id;
        data[1] = old_id;
        execute("UPDATE discord_users SET discord_id = ? WHERE discord_id = ?", data);
    }

    public void banMember(@NotNull User user) {
        String[] data = new String[2];
        data[0] = "1";
        data[1] = user.getId();
        execute("UPDATE discord_users SET banned = ? WHERE discord_id = ?", data);
    }

    public void banMember(@NotNull Member member) {
        String[] data = new String[2];
        data[0] = "1";
        data[1] = member.getId();
        execute("UPDATE discord_users SET banned = ? WHERE discord_id = ?", data);
    }

    public void banMember(@NotNull String id) {
        String[] data = new String[2];
        data[0] = "1";
        data[1] = id;
        execute("UPDATE discord_users SET banned = ? WHERE discord_id = ?", data);
    }

    public void unBanMember(@NotNull User user) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = user.getId();
        execute("UPDATE discord_users SET banned = ? WHERE discord_id = ?", data);
    }

    public void unBanMember(String id) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = id;
        execute("UPDATE discord_users SET banned = ? WHERE discord_id = ?", data);
    }
}
