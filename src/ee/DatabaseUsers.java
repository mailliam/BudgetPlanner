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
        } catch (ClassNotFoundException e) { //Kas peaks muid erroreid ka p��dma?
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DB opened");
    }

    private void createTable() { //kas see peaks private v�i public olema?
        String sql = "CREATE TABLE IF NOT EXISTS USERS (ID INT PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, FIRSTNAME TEXT, LASTNAME TEXT);";
        saveDB(sql);
    }

    private void saveDB(String sql) { //sellise tegemise loogika p�rineb Krister V. sql n�itest
        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //siia tuleb n��d tegelikult kirjutada kontroll, kas kasutaja eksisteerib

    public void registerUser(String userName, String password, String firstName, String lastName) {
        AlertScreens userReg = new AlertScreens();
        String sql = "INSERT INTO USERS (USERNAME, PASSWORD, FIRSTNAME, LASTNAME) VALUES('"+userName+"','"+password+"','"+firstName+"','"+lastName+"')";


            saveDB(sql);
            userReg.userRegistered();
        }

        //Hiljem teen selle asemele �he uue h�piku? see peaks aga �kki hoopis seal screeni all olema.

    public boolean checkUserExistance(String username) {
        try {
            System.out.println(username);
            Statement stat = conn.createStatement();
            String sql = "select exists(SELECT 1 FROM USERS WHERE USERNAME = '"+username+"'); ";
            ResultSet rs = stat.executeQuery(sql);
            Boolean dbUsername = rs.getBoolean(1);
            System.out.println(dbUsername);

            rs.close();
            stat.close();
            return username.equals(dbUsername);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void checkUser() { //see kood p�rineb http://www.tutorialspoint.com/sqlite/sqlite_java.htm
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
