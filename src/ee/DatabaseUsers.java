package ee;

import java.sql.*;

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
        String sql = "CREATE TABLE IF NOT EXISTS USERS (ID INT PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, FIRSTNAME TEXT, LASTNAME TEXT);";
        saveDB(sql);
    }

    private void saveDB(String sql) { //sellise tegemise loogika pärineb Krister V. sql näitest
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
        AlertScreens userReg = new AlertScreens();
        String sql = "INSERT INTO USERS (USERNAME, PASSWORD, FIRSTNAME, LASTNAME) VALUES('"+userName+"','"+password+"','"+firstName+"','"+lastName+"')";
        if (!userExists) {
            saveDB(sql);
            userReg.userRegistered();
            //Hiljem teen selle asemele ühe uue hüpiku? see peaks aga äkki hoopis seal screeni all olema.
        } else {
            userReg.userAlreadyExists();
        }
    }

    private boolean userExists(String userName) {


        return false;
    }

    public void checkUser() { //see kood pärineb http://www.tutorialspoint.com/sqlite/sqlite_java.htm
                              //Katsetan, kas registreeritud tegelased eksisteerivad, see jupp on ainult testi jaoks
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM USERS;");
            while (rs.next()) {
                int id2 = rs.getRow();
                int id = rs.getInt("id");
                String nimi = rs.getString("userName");
                String parool = rs.getString("password");
                System.out.println("ID get row= " +id2);
                System.out.println("ID get int= " +id);
                System.out.println("Name= " +nimi);
                System.out.println("parool= "+parool);
                System.out.println();
            }
            rs.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
