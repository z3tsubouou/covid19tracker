package org.covid.db;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *Өгөгдлийн сантай холболт үүсгэх
 */
public class ConnectionClass {
    public Connection databaseLink;

    /**
     *Өгөгдлийн сантай холбоо үүсгэнэ
     * @return холболт хийх link буцаана
     */
    public Connection getConnection() {
        String dbServer = "remotemysql.com:3306/";
        String dbName = "bxIMbFW2Ke";
        String uName = "bxIMbFW2Ke";
        String uPassword = "6ZKdAIc97r";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection("jdbc:mysql://" + dbServer + dbName, uName, uPassword);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }

}