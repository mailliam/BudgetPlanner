package ee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import java.util.Date;

/**
 * Created by Maila on 28/10/2015.
 */


public class CostInput {

    static HashMap purchaseHeader;
    static HashMap purchaseContent;
    static ArrayList purchase;
    static Date purchaseDate;
    static String inputDate;
    static String buyerName;
    static String storeName;
    static String item;
    static String group;
    static int itemQuantity;
    static int itemPrice;
    static int itemAmount;

    public static void main(String[] args) {

        insertPurchaseHeader();
        insertPurchaseContent();
        createPurchase();

    }

    public static Date understandDateInput() {
        // kasutatud http://www.tutorialspoint.com/java/java_date_time.htm

        Scanner sc = new Scanner(System.in);
        inputDate = sc.nextLine();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        //System.out.println(inputDate);

        try {
            purchaseDate=format.parse(inputDate);
            //System.out.println(purchaseDate);
        } catch (ParseException e) {
            System.out.println("Unparseable using "+format);
        }
        return purchaseDate;
    }

    public static void insertPurchaseHeader() {
        purchaseHeader = new HashMap();

        System.out.print("Insert date: ");
        purchaseDate = understandDateInput();

        System.out.print("Which store did you go to? ");
        Scanner sc = new Scanner(System.in);
        storeName = sc.nextLine();

        System.out.print("Who are you? ");
        buyerName = sc.nextLine();

        purchaseHeader.put("Date", purchaseDate);
        purchaseHeader.put("Store",storeName);
        purchaseHeader.put("Buyer", buyerName);

        System.out.println(purchaseHeader);
    }

    public static void insertPurchaseContent() {
        purchaseContent = new HashMap();
        Scanner sc = new Scanner(System.in);
            System.out.print("What did you buy? ");
            item = sc.nextLine();
            System.out.println("What is the group of this item? ");
            group = sc.nextLine();
            System.out.println("How many items? ");
            itemQuantity = sc.nextInt();
            System.out.println("How much did it cost? ");
            itemPrice = sc.nextInt();
            itemAmount = itemQuantity * itemPrice;

            purchaseContent.put("Item", item);
            purchaseContent.put("Group", group);
            purchaseContent.put("Amount", itemAmount);


    }

    public static void createPurchase(){
        purchase = new ArrayList();
        purchase.add(0, purchaseHeader);
        purchase.add(1,purchaseContent);
        System.out.println(purchase);

    }

}
