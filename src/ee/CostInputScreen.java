package ee;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maila on 19/12/2015.
 */
public class CostInputScreen {

    Stage costInputScreen;
    GridPane purchaseTotal;
    Scene sc;
    int sceneHeight = 1000;
    int sceneWidth = 600;
    int width = sceneWidth/6;
    int rowCounter=0;
    TextField fieldBuyer, fieldStore;
    DatePicker fieldDate;
    TextField[] basketFields;
    GridPane basket = new GridPane();
    Button save;
    int fullRows;


    public CostInputScreen() {
        setupScene();
        purchaseHeaderLabels();
        purchaseHeaderFields();
        purchaseBasket();

    }


    private void setupScene() {
        purchaseTotal = new GridPane();
        costInputScreen = new Stage();
        costInputScreen.setTitle("Cost Input");

        costInputScreen.setOnCloseRequest(event -> {
            costInputScreen.close();
            new ProgramScreen();
        });

        sc = new Scene(purchaseTotal, sceneWidth, sceneHeight);
        costInputScreen.setScene(sc);
        costInputScreen.show();
        purchaseTotal.add(basket,1,3);

    }

    private void purchaseHeaderLabels() {
        HBox h = new HBox();
        Label[] headerLabels = new Label[3];
        for (int i = 0; i < 3; i++) {
            headerLabels[i] = new Label();
            headerLabels[i].setPrefWidth(width);
            h.getChildren().add(headerLabels[i]);
        }
        headerLabels[0].setText("Buyer");
        headerLabels[1].setText("Store");
        headerLabels[2].setText("Date");

        purchaseTotal.add(h,1,1);

    }

    private void purchaseHeaderFields() {
        save = new Button("Save purchase");
        save.setPrefWidth(2*width);
        save.setOnAction(event -> {
            checkFilledRowsCount();
            savePurchaseToDB();
            costInputScreen.close();
            new ProgramScreen();
        });

        HBox h = new HBox();

        fieldBuyer = new TextField();
        fieldBuyer.setPrefWidth(width);
        fieldStore = new TextField();
        fieldStore.setPrefWidth(width);

        fieldDate = new DatePicker(); //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DatePicker.html

        fieldDate.setPrefWidth(width + 50); //kuupäev ei mahu muidu ära
        h.getChildren().addAll(fieldBuyer, fieldStore, fieldDate,save);
        purchaseTotal.add(h,1,2);
    }

    private void purchaseBasket() {

        Label[] basketLabels = new Label[6];
        for (int i = 0; i < 6; i++) {
            basketLabels[i] = new Label();
            basketLabels[i].setPrefWidth(width);
            basket.add(basketLabels[i],i,1);
        }
        basketLabels[0].setText("Row nr.");
        basketLabels[1].setText("Item");
        basketLabels[2].setText("Costgroup");
        basketLabels[3].setText("Quantity");
        basketLabels[4].setText("Price");
        basketLabels[5].setText("Sum");

        purchaseBasketFields();
    }


    private void purchaseBasketFields() {
        basketFields = new TextField[6];

        for (int i = 0; i < 6; i++) { //Kuus tulpa
            basketFields[i] = new TextField();
            basketFields[0].setText(Integer.toString(rowCounter + 1)); //Esimene tulp on mittemuudetav ning seal kajastub alati konkreetse poeskäigu ostukorvi rea number
            basketFields[0].setEditable(false); //kas seda ja eelmist rida yhe reana ei saa kuidagi kirjutada?

            basket.add(basketFields[i], i, rowCounter + 2);
        }

        basket.getChildren().get(basket.getChildren().size()-5).setOnMouseClicked(new EventHandler<MouseEvent>() { //Annan igale loodud "Item" childile võime sellele klikkimisel tekitada uus rida. V6iks toimida nii hiire kui tabiga
            //Basketi suurus esialgu 6 labelit + 6 textfieldi = 0-11 = 12 elementi. Antud v6ime peab tekkima "Item" tf-le, st element nr. 7, mille saan tehte 12 (basketi suurus koos labeitega) - 5 tulemusena.
            @Override
            public void handle(MouseEvent event) {
                rowCounter++;
                purchaseBasketFields();
            }
        });

        basket.getChildren().get(basket.getChildren().size()-7).setOnMouseClicked(event -> {
            calculateRowAmount();
        });

    }

    private void calculateRowAmount() { //Selleks hetkeks, kui kasutaja teoorias jõuab siiani, siis on basket size juba kuue v6rra suurem. vaja on elementi nr. 11, mis on 18-7
        //Vaja on piirata, et kasutaja ei saaks teistel v2ljadel kl6psides seda tekitada
        //Kontrollida, mitmendal real toimus muutus?


        BigDecimal quantity = new BigDecimal(((TextField) basket.getChildren().get(basket.getChildren().size()-9)).getCharacters().toString());
        BigDecimal price = new BigDecimal(((TextField) basket.getChildren().get(basket.getChildren().size()-8)).getCharacters().toString());
        String sum = (quantity.multiply(price)).toString();
        ((TextField) basket.getChildren().get(basket.getChildren().size()-7)).setText(sum);
        ((TextField) basket.getChildren().get(basket.getChildren().size()-7)).setEditable(false);
    }


    private void checkFilledRowsCount() {
        fullRows = 0;
        int koht = 7;
        String itemCheck;
        itemCheck = ((TextField) basket.getChildren().get(koht)).getCharacters().toString();

        while (itemCheck.length()!=0) { //kontrollib, mitu rida on kasutaja üldse ära t2itnud
            koht = koht+6;
            itemCheck = ((TextField) basket.getChildren().get(koht)).getCharacters().toString();
            fullRows++;
        }
    }

    private void savePurchaseToDB() {
        for (int i = 6; i <((fullRows+1)*6); i=i+6) { //Hulk try-catche tuleb siia juurde kirjutada //Kordab seda tsyklit niikaua, kuni on k2idud l2bi k6ik t2idetud read. +1 kujutab endast esimest labelite rida

            String buyer = fieldBuyer.getText();
            String date = fieldDate.getValue().toString();
            String store = fieldStore.getText();
            String item = ((TextField) basket.getChildren().get(i+1)).getCharacters().toString();
            String costGroup = ((TextField) basket.getChildren().get(i+2)).getCharacters().toString();
            BigDecimal quantity = new BigDecimal(((TextField) basket.getChildren().get(i + 3)).getCharacters().toString());
            BigDecimal price = new BigDecimal(((TextField) basket.getChildren().get(i + 4)).getCharacters().toString());
            Databases dbPurchase = new Databases();
            dbPurchase.savePurchase(buyer,date,store,i/6,item,costGroup,quantity,price);
            dbPurchase.registerBuyer(buyer); //registreerib ostjate listi (vajalik p2ringu dropdown jaoks)
            dbPurchase.closeConnection();
        }
    }
}

