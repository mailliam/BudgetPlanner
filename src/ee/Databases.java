package ee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Maila on 25/11/2015.
 */
public class Databases { //Kasutatud Kristeri sql alust
    Connection conn;

    public Databases() {
        createConnection();
        createUsersTable();
        createPurchaseTable(); //tundub nagu mõttetu luua kasutaja juures ostutabelit ja vastupidi
        createBuyersTable();
    }

    private void createConnection() { //Loob ühenduse andmebaasiga
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:budgetplanner.db");
            System.out.println("DB opened");
        } catch (ClassNotFoundException e) { //Kas peaks muid erroreid ka p��dma?
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //K6ik tabelid uuesti luua Collate nocase'na
    //http://stackoverflow.com/questions/973541/how-to-set-sqlite3-to-be-case-insensitive-when-string-comparing

    private void createUsersTable() { //Tekitab kasutajate tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS USERS (ID INT PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, FIRSTNAME TEXT, LASTNAME TEXT);";
        saveDB(sql);
    }

    private void createPurchaseTable() { //Tekitab ostude tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS PURCHASE (BUYER TEXT, DATE TEXT, STORE TEXT, PURCHASEROWID INTEGER, ITEM TEXT, COSTGROUP TEXT, QUANTITY REAL, PRICE REAL);";
        saveDB(sql);
    }

    private void createBuyersTable() { //Tekitab ostude tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS BUYERS (BUYER TEXT);";
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

    public void savePurchase(String buyer, String date, String store, int purchaseRowID, String item, String costGroup, BigDecimal quantity, BigDecimal price) {
        String sql = "INSERT INTO PURCHASE (BUYER, DATE, STORE, PURCHASEROWID, ITEM, COSTGROUP, QUANTITY, PRICE) VALUES('"+buyer+"','"+date+"','"+store+"','"+purchaseRowID+"','"+item+"','"+costGroup+"','"+quantity+"','"+price+"')";
        saveDB(sql);
    }

    public void registerBuyer(String buyer) {
        if(!checkBuyerExistance(buyer)) {
            String sql = "INSERT INTO BUYERS (BUYER) VALUES('"+buyer+"')";
            saveDB(sql);
        }
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
    public boolean checkPassword(String username, String password) { //Kui selline kasutaja on üldse olemas, siis kontrollib, kas parool sisselogimisel on õige
        if(checkUserExistance(username)) {
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

    public boolean checkBuyerExistance(String buyer) { //kontrollib, kas kasutaja on olemas (registreerimisel)
        try {
            System.out.println(buyer);
            Statement stat = conn.createStatement();
            String sql = "SELECT EXISTS(SELECT 1 FROM BUYERS WHERE BUYER = '"+buyer+"'); ";
            ResultSet rs = stat.executeQuery(sql);
            Boolean dbBuyer = rs.getBoolean(1);
            System.out.println(dbBuyer);

            rs.close();
            stat.close();
            return dbBuyer;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList createBuyersList () {
        ArrayList buyersList = new ArrayList();
        int numberOfBuyers = 0;
        String buyer;
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM BUYERS;");
            while (rs.next()) { //boolean
                 numberOfBuyers = numberOfBuyers+1;
            }
            for (int i = 0; i < numberOfBuyers; i++) {
                while(rs.next()) {
                    buyer = rs.getString("BUYER");
                    buyersList.add(i,buyer);
                }
            }

            rs.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buyersList;
    }

    public BigDecimal calculateBuyerAmount(String buyer) {
        BigDecimal amount = new BigDecimal(0);

        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT QUANTITY, PRICE FROM PURCHASE WHERE BUYER = '"+buyer+"' COLLATE NOCASE;"; //Tegelikult tuleks luua uus tabel ja sinna panna collate nocase
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                BigDecimal quantity = new BigDecimal(rs.getBigDecimal("QUANTITY").toString());
                BigDecimal price = new BigDecimal(rs.getBigDecimal("PRICE").toString());
                BigDecimal rowAmount = quantity.multiply(price);
                amount = amount.add(rowAmount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    public BigDecimal calculateCostgroupAmount(String costgroup, String buyer) {
        BigDecimal amount = new BigDecimal(0);

        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT QUANTITY, PRICE FROM PURCHASE WHERE COSTGROUP = '"+costgroup+"' AND BUYER = '"+buyer+"' COLLATE NOCASE;"; //Tegelikult tuleks luua uus tabel ja sinna panna collate nocase
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                BigDecimal quantity = new BigDecimal(rs.getBigDecimal("QUANTITY").toString());
                BigDecimal price = new BigDecimal(rs.getBigDecimal("PRICE").toString());
                BigDecimal rowAmount = quantity.multiply(price);
                amount = amount.add(rowAmount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    public ArrayList<BigDecimal> calculateCostgroupAmountByBuyers(String costgroup) {
        ArrayList list = new ArrayList();
        BigDecimal amountAvo = new BigDecimal(0);
        BigDecimal amountMaila = new BigDecimal(0);

        try {
            Statement stat = conn.createStatement();
            String sqlAvo = "SELECT QUANTITY, PRICE FROM PURCHASE WHERE COSTGROUP = '"+costgroup+"' AND BUYER = 'Avo' COLLATE NOCASE;";
            String sqlMaila = "SELECT QUANTITY, PRICE FROM PURCHASE WHERE COSTGROUP = '"+costgroup+"' AND BUYER = 'Maila' COLLATE NOCASE;";; //Tegelikult tuleks luua uus tabel ja sinna panna collate nocase
            ResultSet rsAvo = stat.executeQuery(sqlAvo);
            while (rsAvo.next()) {
                BigDecimal quantity = new BigDecimal(rsAvo.getBigDecimal("QUANTITY").toString());
                BigDecimal price = new BigDecimal(rsAvo.getBigDecimal("PRICE").toString());
                BigDecimal rowAmount = quantity.multiply(price);
                amountAvo = amountAvo.add(rowAmount);
                list.add(0,amountAvo);
            }
            ResultSet rsMaila = stat.executeQuery(sqlMaila);
            while (rsMaila.next()) {
                BigDecimal quantity = new BigDecimal(rsMaila.getBigDecimal("QUANTITY").toString());
                BigDecimal price = new BigDecimal(rsMaila.getBigDecimal("PRICE").toString());
                BigDecimal rowAmount = quantity.multiply(price);
                amountMaila = amountMaila.add(rowAmount);
                list.add(1,amountMaila);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public void checkUser() { //see kood p�rineb http://www.tutorialspoint.com/sqlite/sqlite_java.htm
        //Katsetan, kas registreeritud tegelased eksisteerivad, see jupp on ainult testi jaoks
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM USERS;");
            while (rs.next()) {
                int id2 = rs.getRow();
                int id = rs.getInt("id");
                String nimi = rs.getString("Username");
                String parool = rs.getString("Password");
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

    public void checkPurchase() {
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM PURCHASE;");
            while (rs.next()) {
                int id2 = rs.getRow();
                int reanr = rs.getInt("purchaserowid");
                String item = rs.getString("item");
                String ostja = rs.getString("buyer");
                String date = rs.getString("date");
                BigDecimal quantity = rs.getBigDecimal("quantity");
                BigDecimal price = rs.getBigDecimal("price");
                String costgroup = rs.getString("costgroup");
                System.out.println("ID get row= " +id2);
                System.out.println("ID get int= " +reanr);
                System.out.println("Ese= " +item);
                System.out.println("Grupp = " +costgroup);
                System.out.println("Ostja= " +ostja);
                System.out.println("Kuupev= "+date);
                System.out.println("Kogus = "+quantity);
                System.out.println("Hind =" +price);
                System.out.println();
            }
            rs.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

