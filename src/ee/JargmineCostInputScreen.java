package ee;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;

/**
 * Created by Maila on 27/12/2015.
 */
public class JargmineCostInputScreen {
    int sceneHeight = 1000;
    int sceneWidth = 700;
    VBox purchase;
    int rowCounter = 0; //vajalik esimeseks loomiseks ära väärtustada
    GridPane basketLabels, basketFields;
    ScrollPane basket;
    TextField[][] tfBasket, tfBasketNew;
    Text alertMessage = new Text();

    TextField tfBuyer, tfStore, tfPurchaseAmount;
    DatePicker dpDate;
    //On see halb siin kirjeldada?


    public JargmineCostInputScreen () {
        setupScene(); //Loob stseeni ostu jaoks
        purchaseHeader(); //Loob ostu p2ise
        purchaseBasketLabels(); //Loob ostukorvi pealkirjad ja teeb ettevalmistuse ostukorvi esimese rea lisamiseks
        purchaseBasketFields(); //Loob ostukorvi esimese rea
    }

    private void setupScene() {
        Stage costInputScreen = new Stage();
        costInputScreen.setTitle("Cost input");
        purchase = new VBox();
        purchase.setStyle("-fx-background-color: #00F00F");
        Scene sc = new Scene(purchase, sceneWidth, sceneHeight);
        costInputScreen.setScene(sc);
        costInputScreen.show();

        costInputScreen.setOnCloseRequest(event -> {
            costInputScreen.close();
            new ProgramScreen();
        });
    }

    private void purchaseHeader() {

        GridPane header = new GridPane();
        header.setHgap(5);
        header.setVgap(5);
        header.setPadding(new Insets(10,10,10,10));

        ColumnConstraints column = new ColumnConstraints();
        column.setPrefWidth(sceneWidth/6.5);

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPrefWidth(sceneWidth/5.5);
        header.getColumnConstraints().addAll(column, column, column3);

        Label l1 = new Label ("Buyer");
        Label l2 = new Label ("Store");
        Label l3 = new Label ("Date");
        header.add(l1,0,0);
        header.add(l2,1,0);
        header.add(l3,2,0);

        tfBuyer = new TextField();
        tfStore = new TextField();
        dpDate = new DatePicker();
        header.add(tfBuyer,0,1);
        header.add(tfStore,1,1);
        header.add(dpDate,2,1);

        header.add(alertMessage, 0, 2); //Koht, kuhu saab hakata kasutajale veateateid kuvama

        Label totalAmount = new Label("Total amount");
        header.add(totalAmount,3,0);

        tfPurchaseAmount = new TextField();
        tfPurchaseAmount.setEditable(false);
        tfPurchaseAmount.setStyle("-fx-background-color: #DADBDE");

        header.add(tfPurchaseAmount,3,1);

        Button btnSaveAndExit = new Button ("Save and finish");
        btnSaveAndExit.setOnAction(event -> {
            savePurchaseToDB();
        });

        header.add(btnSaveAndExit,3,2);


        purchase.getChildren().add(header);
    }

    private void purchaseBasketLabels() {
        basketLabels = new GridPane();
        basketLabels.setHgap(5);
        basketLabels.setVgap(5);
        basketLabels.setPadding(new Insets(10, 10, 10, 10));
        Label[] label = new Label[6];

        for (int i = 0; i < 6; i++) {
            label[i] = new Label();
            basketLabels.add(label[i], i, 0);
            label[i].setPrefWidth(sceneWidth/6.5);
        }
        label[0].setText("Row nr");
        label[1].setText("Item");
        label[2].setText("Category");
        label[3].setText("Quantity");
        label[4].setText("Price");
        label[5].setText("Amount");
        purchase.getChildren().add(basketLabels);

        basketFields = new GridPane(); //Lisaks Labelitele lisab ka esimese osturea parameetrid, vastasel korral hakkasid tulema duplicate childreni veateated, sest sama asja lisati mitu korda.
        basketFields.setHgap(5);
        basketFields.setVgap(5);
        basketFields.setPadding(new Insets(10, 10, 10, 10));
        basket = new ScrollPane();
        basket.setStyle("-fx-background: #00F00F");
        purchase.getChildren().add(basket);
    }

    private void purchaseBasketFields() {
        tfBasket = new TextField[rowCounter+1][6]; //loob uue objekti igal korral, vanad sisestatud v22rtused kaovad sellega m2lust

        for (int i = rowCounter; i < rowCounter+1; i++) {
            if(rowCounter > 0) {   // asendab loodud objekti tyhjad v22rtused kasutaja poolt eelnevalt sisestatuga kuni viimase reani (v2ljaarvatud).
                for (int k = 0; k < rowCounter; k++) {
                    for (int l = 0; l < 6; l++) {
                        tfBasket[k][l] = tfBasketNew[k][l];
                    }
                }
            }
            for (int j = 0; j < 6; j++) {
                tfBasket[i][j] = new TextField();
                tfBasket[i][j].setPrefWidth(sceneWidth/7);
                tfBasket[i][0].setText(Integer.toString(rowCounter+1));
                tfBasket[i][0].setEditable(false);
                basketFields.add(tfBasket[i][j], j, i);
                basket.setContent(basketFields); //lisab ostukorvile, mis on ScrollPane, textFieldid
            }
            tfBasketNew = tfBasket; //kopeerib kasutaja poolt varasemalt sisestatud v22rtuste massiivi
        }

        tfBasket[rowCounter][4].setOnAction(event -> {
            tfBasket[rowCounter][4].setOnAction(null);   //Yks vastustest: http://stackoverflow.com/questions/14927233/programmatically-remove-a-listener-added-using-fxml
            rowCounter++;
            purchaseBasketFields();
        });

        tfBasket[rowCounter][3].textProperty().addListener((observable, oldValue, newValue) -> {
            calculateRowAmount();
        });

        tfBasket[rowCounter][4].textProperty().addListener((observable, oldValue, newValue) -> {
            calculateRowAmount();
        });

    }

    private void calculateRowAmount() { //Arvutab rea summa, korrutades hinna ja koguse
        BigDecimal purchaseAmount = new BigDecimal(0);
        for (int i = 0; i < rowCounter + 1; i++) {

            if (!tfBasket[i][3].getText().isEmpty() && !tfBasket[i][4].getText().isEmpty()) { //Kontrollib, kas kasutaja on mõlemad arvutuseks vajalikud v22rtused on sisestatud

                for (int u = 0; u < tfBasket[i][3].getText().length(); u++) {  //Asendab vajadusel koguse puhul kasutaja sisestatud ',' '.'.ga
                    String text = tfBasket[i][3].getText();
                    char ch = text.charAt(u);
                    if (ch == ',') {
                        tfBasket[i][3].setText(text.replace(text.charAt(u),'.'));
                    }
                }

                for (int u = 0; u < tfBasket[i][4].getText().length(); u++) {  //Asendab vajadusel hinna puhul kasutaja sisestatud ',' '.'.ga
                    String text = tfBasket[i][4].getText();
                    char ch = text.charAt(u);
                    if (ch == ',') {
                        tfBasket[i][4].setText(text.replace(text.charAt(u),'.'));
                    }
                }

                try {
                    BigDecimal quantity = new BigDecimal(tfBasket[i][3].getText());
                    BigDecimal price = new BigDecimal(tfBasket[i][4].getText());
                    BigDecimal amount = quantity.multiply(price);
                    purchaseAmount = purchaseAmount.add(amount);
                    tfBasket[i][5].setText(amount.toString());
                    tfBasket[i][5].setEditable(false);
                    tfBasket[i][3].setStyle(null);
                    tfBasket[i][4].setStyle(null);
                    alertMessage.setText(null); //Kui kasutaja tuleb selle peale, et enne parandamist sisestada uus rida, siis kaob vahepeal alert 2ra.
                } catch (java.lang.NumberFormatException e) {
                    alertMessage.setText("Quantity or price format is incorrect: must be number");
                    alertMessage.setFill(Color.RED);
                    tfBasket[i][3].setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                    tfBasket[i][4].setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                }
            }
        }
        tfPurchaseAmount.setText(purchaseAmount.toString());

    }

    private boolean checkInsertionCorrectness () {

        if(tfBuyer.getText().isEmpty() || tfStore.getText().isEmpty() || dpDate.getValue() == null) { //Kui kuup2ev pole valitud, siis == null: http://code.makery.ch/blog/javafx-8-date-picker/
            alertMessage.setText("Buyer, store and date must be selected");
            alertMessage.setFill(Color.RED);
            return false;
        } else {
            LocalDate today = LocalDate.now(); //http://www.java2s.com/Tutorials/Java/java.time/LocalDate/index.htm
            if(dpDate.getValue().isAfter(today)) {
                alertMessage.setText(null);
                alertMessage.setText("Purchase date can not be in the future");
                alertMessage.setFill(Color.RED);
                return false;
            } else {
                for (int i = 0; i < rowCounter+1; i++) { //Selle lohe asemel v6iks midagi normaalset olla
                    if( (tfBasket[i][1].getText().isEmpty() && tfBasket[i][2].getText().isEmpty() && tfBasket[i][3].getText().isEmpty() && tfBasket[i][4].getText().isEmpty() && tfBasket[i][5].getText().isEmpty()) || (!tfBasket[i][1].getText().isEmpty() && !tfBasket[i][2].getText().isEmpty() && !tfBasket[i][3].getText().isEmpty() && !tfBasket[i][4].getText().isEmpty() && !tfBasket[i][5].getText().isEmpty())) {

                    } else {
                        alertMessage.setText("You have some unfilled rows");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void savePurchaseToDB() {

        if (checkInsertionCorrectness()) { //Kui kontroll on andnud 6ige tulemuse

            for (int i = 0; i < rowCounter + 1; i++) { //Kordab seda tsyklit niikaua, kuni on k2idud l2bi k6ik t2idetud read.
                if  (!tfBasket[i][1].getText().isEmpty() && !tfBasket[i][2].getText().isEmpty() && !tfBasket[i][3].getText().isEmpty() && !tfBasket[i][4].getText().isEmpty() && !tfBasket[i][5].getText().isEmpty()) {
                    Databases db = new Databases(); //Tundub kahtlane tsyklis connectionit avada

                    String buyer = tfBuyer.getText();
                    String date = dpDate.getValue().toString();
                    String store = tfStore.getText();


                    int basketRowNr = Integer.parseInt(tfBasket[i][0].getText());
                    String item = tfBasket[i][1].getText();
                    String category = tfBasket[i][2].getText();
                    BigDecimal quantity = new BigDecimal(tfBasket[i][3].getText());
                    BigDecimal basketRowAmount = new BigDecimal(tfBasket[i][5].getText());

                    db.registerBuyer(buyer); //registreerib ostjate listi (vajalik p2ringu dropdown jaoks)
                    db.savePurchaseBasket(basketRowNr, buyer, date, store, item, category, quantity, basketRowAmount);
                    db.registerItem(item, category);
                    db.closeConnection();
                }
            }
        }
    }

}
