package ee;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Maila on 25/11/2015.
 *
 * Andmebaasiga seotud toimingud
 * Kasutatud i200 sql näite alust ja http://www.tutorialspoint.com/sqlite/sqlite_java.htm
 *
 */
public class Databases {
    Connection conn;

    public Databases() {
        createConnection();
        createUsersTable(); //Kasutajate tabel koos paroolide, nime jms-ga
        createBuyersTable(); //Ostjate tabel
        createItemsTable(); //Kaupade tabel
        createPurchaseBasketTable(); //Osturidade tabel
    }

    //Loob ühenduse andmebaasiga
    private void createConnection() {
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

    private void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS USERS (USERNAME TEXT COLLATE NOCASE, PASSWORD TEXT, FIRSTNAME TEXT COLLATE NOCASE, LASTNAME TEXT COLLATE NOCASE);";
        saveDB(sql);
    }

    private void createBuyersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS BUYERS (BUYER TEXT COLLATE NOCASE);";
        saveDB(sql);
    }

    private void createItemsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS ITEMS (ITEM TEXT COLLATE NOCASE, CATEGORY TEXT COLLATE NOCASE);";
        saveDB(sql);
    }

    private void createPurchaseBasketTable() {
        String sql = "CREATE TABLE IF NOT EXISTS BASKETS (PURCHASEROWID INTEGER, BUYER TEXT COLLATE NOCASE, DATE TEXT, " +
                "STORE TEXT COLLATE NOCASE, ITEM TEXT COLLATE NOCASE, CATEGORY TEXT COLLATE NOCASE, QUANTITY REAL, " +
                "ROWAMOUNT REAL);";
        saveDB(sql);
    }

    //Salvestab andmebaasi.. Loogika pärineb i200 sql näitest.
    private void saveDB(String sql) {
        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void registerUser(String userName, String password, String firstName, String lastName) {
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

    //Kontrollib kasutaja olemasolu
    public boolean checkUserExistance(String username) {
        try {
            System.out.println(username);
            Statement stat = conn.createStatement();
            String sql = "SELECT EXISTS(SELECT 1 FROM USERS WHERE USERNAME = '"+username+"'); "; //http://stackoverflow.com/questions/9755860/valid-query-to-check-if-row-exists-in-sqlite3
            ResultSet rs = stat.executeQuery(sql);
            Boolean dbUserExists = rs.getBoolean(1);
            System.out.println(dbUserExists);

            rs.close();
            stat.close();
            return dbUserExists;

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return true;
    }

    //Kontrollib kasutaja olemasolul parooli õigsust
    public boolean checkPassword(String username, String password) {
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
                System.exit(0);
            }
        }
        return false;
    }

    //Kustutab kasutaja kasutajate tabelist
    public void deleteUser(String username) {  //http://www.tutorialspoint.com/sqlite/sqlite_java.htm
        try {
            Statement stat = conn.createStatement();
            String sql = "DELETE FROM USERS WHERE USERNAME = '"+username+"';";
            stat.executeUpdate(sql);
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    //Kontrollib ostja olemasolu
    public boolean checkBuyerExistance(String buyer) {
        try {
            System.out.println(buyer);
            Statement stat = conn.createStatement();
            String sql = "SELECT EXISTS(SELECT 1 FROM BUYERS WHERE BUYER = '"+buyer+"'); ";
            ResultSet rs = stat.executeQuery(sql);
            Boolean dbBuyerExists = rs.getBoolean(1);
            System.out.println(dbBuyerExists);

            rs.close();
            stat.close();
            return dbBuyerExists;

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return true;
    }

    //Kontrollib, kas tabelis on mõni ost
    public boolean checkPurchaseExistance() {
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT EXISTS(SELECT 1 FROM BASKETS); ";
            ResultSet rs = stat.executeQuery(sql);
            Boolean dbPurchaseExists = rs.getBoolean(1);
            System.out.println(dbPurchaseExists);

            rs.close();
            stat.close();
            return dbPurchaseExists;

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return false;
    }

    //Kontrollib, kas ese on olemas
    public boolean checkItemExistance(String item) {
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
            System.exit(0);
        }
        return true;
    }

    //Tekitab konkreetses kategoorias olevate esemente nimekirja
    public ArrayList getItemListForCategory(String category) {
        ArrayList itemList = new ArrayList();

        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT ITEM FROM ITEMS WHERE CATEGORY = '"+category+"';";
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()) {
                String item = rs.getString("ITEM");
                itemList.add(item);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return itemList;
    }

    //Tekitab ostjate nimekirja
    public ArrayList getBuyerList() {
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
            System.exit(0);
        }
        return buyerList;
    }

    //Leiab, millisesse kategooriasse ese kuulub
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
            System.exit(0);
        }
        return null;
    }

    //Tekitab kategooriate nimekirja
    public ArrayList getCategoryList() {
        ArrayList categoryList = new ArrayList();

        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT DISTINCT CATEGORY FROM ITEMS"; //unikaalsete väärtuste leidmine: http://stackoverflow.com/questions/4790162/sqlite-select-distinct-values-of-a-column-without-ordering
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()) {
                String category = rs.getString("CATEGORY");
                categoryList.add(category);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return categoryList;
    }

    //Arvutab perioodi summa kategooria kohta
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
            System.exit(0);
        }
        return amount;
    }

    //Arvutab perioodi summa ostja kohta
    public BigDecimal getPeriodAmountByBuyers(String buyer, LocalDate start, LocalDate end) {
        BigDecimal amount = new BigDecimal(0);
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT ROWAMOUNT FROM BASKETS WHERE BUYER = '"+buyer+"' AND DATE BETWEEN '"+start+"' and '"+end+"'; ";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                BigDecimal rowAmount = new BigDecimal(rs.getBigDecimal("ROWAMOUNT").toString());
                amount = amount.add(rowAmount);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return amount;
    }

    //Arvutab perioodi summa ostja ja kategooria kohta
    public BigDecimal getPeriodAmountCategoryByBuyers(String category, String buyer, LocalDate start, LocalDate end) {
        BigDecimal amount = new BigDecimal(0);
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT ROWAMOUNT FROM BASKETS WHERE CATEGORY = '"+category+"' AND BUYER = '"+buyer+"' " +
                    "AND DATE BETWEEN '"+start+"' and '"+end+"'; ";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                BigDecimal rowAmount = new BigDecimal(rs.getBigDecimal("ROWAMOUNT").toString());
                amount = amount.add(rowAmount);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return amount;
    }

    //Arvutab perioodi summa eseme kohta
    public BigDecimal getPeriodAmountByItems(String item, LocalDate start, LocalDate end) {
        BigDecimal amount = new BigDecimal(0);
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT ROWAMOUNT FROM BASKETS WHERE ITEM = '"+item+"' AND DATE BETWEEN '"+start+"' and '"+end+"'; ";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                BigDecimal rowAmount = new BigDecimal(rs.getBigDecimal("ROWAMOUNT").toString());
                amount = amount.add(rowAmount);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return amount;
    }

    //Arvutab perioodi summa
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
            System.exit(0);
        }
        return periodAmount;
    }

    //Sulgeb ühenduse
    public void closeConnection() {
        try {
            conn.close();
            System.out.println("DB closed");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

}

