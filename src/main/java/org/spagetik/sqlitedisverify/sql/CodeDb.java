package org.spagetik.sqlitedisverify.sql;

import java.util.UUID;

public class CodeDb extends AbstaractDb{

    public CodeDb(String path, String name) {
        super(path, name);
        execute("CREATE TABLE IF NOT EXISTS verify_codes (uuid varchar(36) NOT NULL, code varchar(6) NOT NULL)");
    }

    public void addCodeToDb (UUID uuid, int code) {
        String[] data = new String[2];
        data[0] = String.valueOf(uuid);
        data[1] = String.valueOf(code);
        execute("INSERT INTO verify_codes (uuid, code) VALUES (?, ?)", data);
    }

    public void removeCodeFromDb (int code) {
        String[] data = new String[1];
        data[0] = String.valueOf(code);
        execute("DELETE FROM verify_codes WHERE code = ?", data);
    }

    public UUID checkCode(String code) {
        String[] data = new String[1];
        data[0] = String.valueOf(code);
        try {
            return UUID.fromString(executeSelect("SELECT uuid FROM verify_codes WHERE code = ?", data, 1).get(0).get("0"));
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
