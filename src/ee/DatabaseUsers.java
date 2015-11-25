package ee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Maila on 25/11/2015.
 */
public class DatabaseUsers {
    Connection conn;

    public DatabaseUsers() {
        createConnection();
        createTable();
    }

    private void createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:budgetplanner.db");
        } catch (ClassNotFoundException e) { //Kas peaks muid erroreid ka püüdma?
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DB opened");
    }

    private void createTable() { //kas see peaks private või public olema?
        String sql = "CREATE TABLE IF NOT EXISTS USERS (ID INT AUTO_INCREMENT, USERNAME TEXT, PASSWORD TEXT, FIRSTNAME TEXT, LASTNAME TEXT);";
        saveDB(sql);
    }

    private void saveDB(String sql) {
        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //siia tuleb nüüd tegelikult kirjutada kontroll, kas kasutaja eksisteerib

    public void registerUser(String userName, String password, String firstName, String lastName) {
        String sql = "INSERT INTO USERS (USERNAME, PASSWORD, FIRSTNAME, LASTNAME) VALUES('"+userName+"','"+password+"','"+firstName+"','"+lastName+"')";
        saveDB(sql);
        System.out.println("User registered"); //Hiljem teen selle asemele ühe uue hüpiku? see peaks aga äkki hoopis seal screeni all olema.
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DB closed");
    }
}
