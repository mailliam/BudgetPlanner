package ee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Maila on 12/12/2015.
 */
public class DatabasePurchase { //Kasutatud Kristeri sql alust
    Connection conn;

    public DatabasePurchase() {
        createConnection();
        createTable();
    }

    private void createConnection() { //Loob ühenduse andmebaasiga
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:budgetplanner.db");
        } catch (ClassNotFoundException e) { //Kas peaks muid erroreid ka p??dma?
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DB Purchase opened");
    }

    private void createTable() { //Tekitab ostude tabeli kui seda veel ei ole
        String sql = "CREATE TABLE IF NOT EXISTS PURCHASE (BUYER TEXT, DATE INTEGER, STORE TEXT, PURCHASEROWID INTEGER, ITEM TEXT, COSTGROUP TEXT, QUANTITY REAL, PRICE REAL);";
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

    public void savePurchase(String buyer, int date, String store, int purchaseRowID, String item, String costgroup, double quantity, double price) {
        String sql = "INSERT INTO PURCHASE (BUYER, DATE, STORE, PURCHASEROWID, ITEM, COSTGROUP, QUANTITY, PRICE) VALUES('"+buyer+"','"+date+"','"+store+"','"+purchaseRowID+",'"+item+"','"+costgroup+"','"+quantity+"','"+price+"')";
        saveDB(sql);

    }


    public void closeConnection() { //sulgeb baasi ühenduse. Tegelikult ei pea see olema üldse ju konkreetse baasiga seotud. Yhenduse sulgemine võikski olla eraldi klassis. samamoodi avamine ja salvestamine..
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DB Purchase closed");
    }


}
