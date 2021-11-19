package org.spagetik.sqlitedisverify.sql;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract class AbstaractDb {

    private final SQLiteDataSource sqLiteDataSource;
    private Connection connection;

    public AbstaractDb(String path, String name) {
        sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl("jdbc:sqlite:" + path + "/" + name + ".db");
    }

    public void getConnection() {
        try {
            connection = sqLiteDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HashMap<String, String>> executeSelect(String sqlRequest, String[] data, int columnsNum) {
        getConnection();
        PreparedStatement prSt;
        List<HashMap<String, String>> resultList = new ArrayList<>();
        try {
            prSt = connection.prepareStatement(sqlRequest);
            for(int i = 0; i < data.length; i++) {
                prSt.setString(i+1, data[i]);
            }
            ResultSet resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                HashMap<String, String> row = new HashMap<String, String>();
                for(int i = 0; i < columnsNum; i++) {
                    row.put(String.valueOf(i), resultSet.getString(i+1));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return resultList;
    }

    public List<HashMap<String, String>> executeSelect(String sqlRequest, int columnsNum) {
        getConnection();
        PreparedStatement prSt;
        List<HashMap<String, String>> resultList = new ArrayList<>();
        try {
            prSt = connection.prepareStatement(sqlRequest);
            ResultSet resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                HashMap<String, String> row = new HashMap<String, String>();
                for(int i = 0; i < columnsNum; i++) {
                    row.put(String.valueOf(i), resultSet.getString(i+1));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return resultList;
    }

    public void execute(String sqlRequest, String[] data) {
        getConnection();
        PreparedStatement prSt;
        try {
            prSt = connection.prepareStatement(sqlRequest);
            for(int i = 0; i < data.length; i++) {
                prSt.setString(i+1, data[i]);
            }
            prSt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public void execute(String sqlRequest) {
        getConnection();
        PreparedStatement prSt;
        try {
            prSt = connection.prepareStatement(sqlRequest);
            prSt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }
}
