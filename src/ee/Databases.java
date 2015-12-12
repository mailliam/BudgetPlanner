package ee;

import java.sql.*;

/**
 * Created by Maila on 25/11/2015.
 */
public class Databases { //Kasutatud Kristeri sql alust
    Connection conn;

    public Databases() {
        createConnection();
        createUsersTable();
        createPurchaseTable(); //tundub nagu mõttetu luua kasutaja juures ostutabelit ja vastupidi
    }

    private void createConnection() { //Loob ühenduse andmebaasiga
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

    private void createUsersTable() { //Tekitab kasutajate tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS USERS (ID INT PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, FIRSTNAME TEXT, LASTNAME TEXT);";
        saveDB(sql);
    }

    private void createPurchaseTable() { //Tekitab ostude tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS PURCHASE (BUYER TEXT, DATE INTEGER, STORE TEXT, PURCHASEROWID INTEGER, ITEM TEXT, COSTGROUP TEXT, QUANTITY REAL, PRICE REAL);";
        saveDB(sql);
    }


    private void saveDB(String sql) { //sellise tegemise loogika p�rineb Krister V. sql n�itest. salvestab andmebaasi
        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(String userName, String password, String firstName, String lastName) { //sisestab kasutaja kirje andmebaasi tabelisse
        AlertScreens as = new AlertScreens();
        String sql = "INSERT INTO USERS (USERNAME, PASSWORD, FIRSTNAME, LASTNAME) VALUES('"+userName+"','"+password+"','"+firstName+"','"+lastName+"')";
            saveDB(sql);
            as.userRegistered();
        }

    public void savePurchase(String buyer, int date, String store, int purchaseRowID, String item, String costgroup, double quantity, double price) {
        String sql = "INSERT INTO PURCHASE (BUYER, DATE, STORE, PURCHASEROWID, ITEM, COSTGROUP, QUANTITY, PRICE) VALUES('"+buyer+"','"+date+"','"+store+"','"+purchaseRowID+",'"+item+"','"+costgroup+"','"+quantity+"','"+price+"')";
        saveDB(sql);

    }

    public boolean checkUserExistance(String username) { //kontrollib, kas kasutaja on olemas (registreerimisel)
        try {
            System.out.println(username);
            Statement stat = conn.createStatement();
            String sql = "SELECT EXISTS(SELECT 1 FROM USERS WHERE USERNAME = '"+username+"'); ";
            ResultSet rs = stat.executeQuery(sql);
            Boolean dbUsername = rs.getBoolean(1);
            System.out.println(dbUsername);

            rs.close();
            stat.close();
            return dbUsername;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkPassword(String username, String password) { //Kontrollib, kas parool sisselogimisel on õige
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT PASSWORD FROM USERS WHERE USERNAME = '"+username+"';";
            ResultSet rs = stat.executeQuery(sql);
            String dbPassword = rs.getString("PASSWORD");
            rs.close();
            stat.close();
            return dbPassword.equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteUser(String username) { //Kustutab kasutaja kasutajate tabelist //http://www.tutorialspoint.com/sqlite/sqlite_java.htm
        try {
            Statement stat = conn.createStatement();
            String sql = "DELETE FROM USERS WHERE USERNAME = '"+username+"';";
            stat.executeUpdate(sql);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void closeConnection() { //sulgeb baasi ühenduse
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DB closed");
    }


}
