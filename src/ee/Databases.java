package ee;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Maila on 25/11/2015.
 */
public class Databases { //Kasutatud Kristeri sql alust
    Connection conn;

    public Databases() {
        createConnection(); //tundub mõttetu luua kasutaja juures ostutabelit ja vastupidi
        createUsersTable(); //Kasutajate tabel koos paroolide, nime jms-ga
        createBuyersTable(); //Ostjate tabel: ostja != alati kasutaja
        createItemsTable(); //Kaupade tabel
        createPurchaseBasketTable(); //Osturidade tabel, ehk mida-kui palju-mis hinnaga
    }

    private void createConnection() { //Loob ühenduse andmebaasiga
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:budgetplanner.db");
            System.out.println("DB opened");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Collate nocase defineerimine, et ei tekiks uusi ridu, kui kasutaja kogemata valesti sisestab: http://stackoverflow.com/questions/973541/how-to-set-sqlite3-to-be-case-insensitive-when-string-comparing

    private void createUsersTable() { //Tekitab kasutajate tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS USERS (USERNAME TEXT COLLATE NOCASE, PASSWORD TEXT, FIRSTNAME TEXT COLLATE NOCASE, LASTNAME TEXT COLLATE NOCASE);";
        saveDB(sql);
    }

    private void createBuyersTable() { //Tekitab ostjate tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS BUYERS (BUYER TEXT COLLATE NOCASE);";
        saveDB(sql);
    }

    private void createItemsTable() { //Tekitab esemete tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS ITEMS (ITEM TEXT COLLATE NOCASE, CATEGORY TEXT COLLATE NOCASE);";
        saveDB(sql);
    }

    private void createPurchaseBasketTable() { //Tekitab osturidade tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS BASKETS (PURCHASEROWID INTEGER, BUYER TEXT COLLATE NOCASE, DATE TEXT, " +
                "STORE TEXT COLLATE NOCASE, ITEM TEXT COLLATE NOCASE, CATEGORY TEXT COLLATE NOCASE, QUANTITY REAL, " +
                "ROWAMOUNT REAL);";
        saveDB(sql);
    }

    private void saveDB(String sql) { //sellise tegemise loogika p2rineb Krister V. sql n2itest. salvestab andmebaasi
        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(String userName, String password, String firstName, String lastName) { //sisestab kasutaja kirje kasutaja tabelisse
        AlertScreens as = new AlertScreens();
        String sql = "INSERT INTO USERS (USERNAME, PASSWORD, FIRSTNAME, LASTNAME) VALUES('"+userName+"','"+password+"','"+firstName+"','"+lastName+"')";
        saveDB(sql);
        as.userRegistered();
    }

    public void savePurchaseBasket(int basketRowNr, String buyer, String date, String store, String item, String category, BigDecimal quantity, BigDecimal amount) {
        String sql = "INSERT INTO BASKETS (PURCHASEROWID, BUYER, DATE, STORE, ITEM, CATEGORY, QUANTITY, ROWAMOUNT) VALUES('"+basketRowNr+"','"+buyer+"','"+date+"','"+store+"','"+item+"','"+category+"','"+quantity+"','"+amount+"')";
        saveDB(sql);
    }

    public void registerBuyer(String buyer) {
        if(!checkBuyerExistance(buyer)) {
            String sql = "INSERT INTO BUYERS (BUYER) VALUES('"+buyer+"')";
            saveDB(sql);
        }
    }

    public void registerItem(String item, String category) {
        if(!checkItemExistance(item)) {
            String sql = "INSERT INTO ITEMS (ITEM, CATEGORY) VALUES('"+item+"','"+category+"')";
            saveDB(sql);
        }
    }

    public boolean checkUserExistance(String username) { //Kontrollib, kas kasutaja on olemas
        try {
            System.out.println(username);
            Statement stat = conn.createStatement();
            String sql = "SELECT EXISTS(SELECT 1 FROM USERS WHERE USERNAME = '"+username+"'); "; //http://stackoverflow.com/questions/9755860/valid-query-to-check-if-row-exists-in-sqlite3
            ResultSet rs = stat.executeQuery(sql);
            Boolean dbUsername = rs.getBoolean(1);
            System.out.println(dbUsername);

            rs.close();
            stat.close();
            return dbUsername;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; //true, et vea korral midagi ei registreeritaks
    }

    public boolean checkPassword(String username, String password) { //Kui kasutaja on olemas, siis kontrollib, kas parool sisselogimisel/kasutaja kustutamisel on õige
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

    public boolean checkBuyerExistance(String buyer) { //Kontrollib, kas kasutaja on olemas
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
        return true;  //true, et vea korral midagi ei registreeritaks
    }

    public boolean checkItemExistance(String item) { //Kontrollib, kas ese on olemas
        try {
            System.out.println(item);
            Statement stat = conn.createStatement();
            String sql = "SELECT EXISTS(SELECT 1 FROM ITEMS WHERE ITEM = '"+item+"'); ";
            ResultSet rs = stat.executeQuery(sql);
            Boolean dbItemExists = rs.getBoolean(1);
            System.out.println(dbItemExists);

            rs.close();
            stat.close();
            return dbItemExists;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;  //true, et vea korral midagi ei registreeritaks
    }

    public BigDecimal getBuyerAmount (String buyer) {
        BigDecimal buyerAmount = new BigDecimal(0);

        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT ROWAMOUNT FROM BASKETS WHERE BUYER = '"+buyer+"';";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                BigDecimal rowAmount = new BigDecimal(rs.getBigDecimal("ROWAMOUNT").toString());
                buyerAmount = buyerAmount.add(rowAmount);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buyerAmount;
    }

    public ArrayList getBuyerList() { //Ostjate tabel on tegelikult m6ttetu ja v6iks samamoodi distinktiivselt selekteerida.
        ArrayList buyerList = new ArrayList();

        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT BUYER FROM BUYERS";
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()) {
                String buyer = rs.getString("BUYER");
                buyerList.add(buyer);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buyerList;
    }

    public String getCategoryForItem(String item) {
        try {
            if(checkItemExistance(item)) {
                String category;
                Statement stat = conn.createStatement();
                String sql = "SELECT CATEGORY FROM ITEMS WHERE ITEM = '"+item+"';";
                ResultSet rs = stat.executeQuery(sql);
                category = rs.getString("CATEGORY");
                rs.close();
                stat.close();
                return category;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList getCategoryList() {
        ArrayList categoryList = new ArrayList();

        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT DISTINCT CATEGORY FROM ITEMS"; //unikaalsete v22rtuste leidmine: http://stackoverflow.com/questions/4790162/sqlite-select-distinct-values-of-a-column-without-ordering
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()) {
                String category = rs.getString("CATEGORY");
                categoryList.add(category);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryList;
    }

    public BigDecimal getPeriodAmountByCategories(String category, LocalDate start, LocalDate end) {
        BigDecimal amount = new BigDecimal(0);
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT ROWAMOUNT FROM BASKETS WHERE CATEGORY = '"+category+"' AND DATE BETWEEN '"+start+"' and '"+end+"'; ";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                BigDecimal rowAmount = new BigDecimal(rs.getBigDecimal("ROWAMOUNT").toString());
                amount = amount.add(rowAmount);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    public BigDecimal getPeriodAmount(LocalDate start, LocalDate end){
        BigDecimal periodAmount = new BigDecimal(0);

        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT ROWAMOUNT FROM BASKETS WHERE DATE BETWEEN '"+start+"' and '"+end+"';";
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()) {
                BigDecimal rowAmount = new BigDecimal(rs.getBigDecimal("ROWAMOUNT").toString());
                periodAmount = periodAmount.add(rowAmount);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return periodAmount;
    }

    public BigDecimal calculateCostgroupAmount(String costgroup, String buyer) { //Hetkel kasutu
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
    } //Jama

    public ArrayList<BigDecimal> calculateCostgroupAmountByBuyers(String costgroup) { //Hetkel kasutu
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
    } //Jama

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
            System.out.println("DB closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkPurchase() {
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM BASKETS;");
            while (rs.next()) {
                int id2 = rs.getRow();
                int reanr = rs.getInt("purchaserowid");
                String item = rs.getString("item");
                String ostja = rs.getString("buyer");
                String date = rs.getString("date");
                BigDecimal quantity = rs.getBigDecimal("quantity");

                String costgroup = rs.getString("CATEGORY");
                System.out.println("ID get row= " +id2);
                System.out.println("ID get int= " +reanr);
                System.out.println("Ese= " +item);
                System.out.println("Grupp = " +costgroup);
                System.out.println("Ostja= " +ostja);
                System.out.println("Kuupev= "+date);
                System.out.println("Kogus = "+quantity);

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

