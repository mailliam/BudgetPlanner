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
 *
 * Aken, mis vıimaldab kasutajal sisestada uusi kulusid
 *
 */
public class CostInputScreen {
    Stage costInputScreen;
    int sceneHeight = 700;
    int sceneWidth = 700;
    VBox purchase;
    int rowCounter = 0; //vajalik esimeseks loomiseks ‰ra v‰‰rtustada
    GridPane basketLabels, basketFields;
    ScrollPane basket;
    TextField[][] tfBasket, tfBasketNew;
    Text alertMessage = new Text();

    TextField tfBuyer, tfStore, tfPurchaseAmount;
    DatePicker dpDate;

    public CostInputScreen() {
        setupScene(); //Loob stseeni ostu jaoks
        purchaseHeader(); //Loob ostu p‰ise
        purchaseBasketLabels(); //Loob ostukorvi pealkirjad ja teeb ettevalmistuse ostukorvi esimese rea lisamiseks
        purchaseBasketFields(); //Loob ostukorvi esimese rea
    }

    //Stseeni seadistus koos peamise layoutiga
    private void setupScene() {
        costInputScreen = new Stage();
        costInputScreen.setTitle("Cost input");
        purchase = new VBox();

        Scene sc = new Scene(purchase, sceneWidth, sceneHeight);
        sc.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        //CSS lisamine:
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph

        costInputScreen.setScene(sc);
        costInputScreen.show();

        costInputScreen.setOnCloseRequest(event -> {
            costInputScreen.close();
            new ProgramScreen();
        });
    }

    //Meetod loob ostup‰ise - kes, kus, millal, summa kokku
    private void purchaseHeader() {

        GridPane header = new GridPane();
        header.setHgap(5);
        header.setVgap(5);
        header.setPadding(new Insets(10,10,10,10));

        ColumnConstraints column = new ColumnConstraints();
        column.setPrefWidth(sceneWidth/6.5);

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPrefWidth(sceneWidth/4);
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

        header.add(alertMessage, 0, 2);                 //Koht, kuhu saab hakata kasutajale veateateid kuvama

        Label totalAmount = new Label("Total amount");
        header.add(totalAmount,3,0);

        tfPurchaseAmount = new TextField();
        tfPurchaseAmount.setEditable(false);            //Kasutaja ei pea saama seda muuta, automaatne
        tfPurchaseAmount.setStyle("-fx-background-color: #DADBDE");

        header.add(tfPurchaseAmount,3,1);

        Button btnSaveAndExit = new Button ("Save and finish");
        btnSaveAndExit.setOnAction(event -> {
            savePurchaseToDB();
        });

        header.add(btnSaveAndExit,3,2);

        purchase.getChildren().add(header);
    }

    //Meetod loob ostukorvi pealkirjad ning lisaks defineerib esimese osturea muutujad/parameetrid ning lisab ostule
    //ostukorvi basket, mis on ScrollPane. Vastasel korral tekiks j‰rgneva meetodi k‰itamisel duplicate children viga.
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

        basketFields = new GridPane();
        basketFields.setHgap(5);
        basketFields.setVgap(5);
        basketFields.setPadding(new Insets(10, 10, 10, 10));
        basket = new ScrollPane();
        purchase.getChildren().add(basket);
    }

    //Meetod loob ostukorvi v‰ljad
    private void purchaseBasketFields() {
        tfBasket = new TextField[rowCounter+1][6];              //Iga kord, kui meetod esile kutsutakse, luuakse uus TextFieldide massiiv tfBasket[][], vanad sisestatud v‰‰rtused kaovad sellega m‰lust

        for (int i = rowCounter; i < rowCounter+1; i++) {
            if(rowCounter > 0) {                                // asendab loodud objekti tyhjad v‰‰rtused kasutaja poolt eelnevalt sisestatuga kuni viimase reani (v‰ljaarvatud).
                for (int k = 0; k < rowCounter; k++) {
                    for (int l = 0; l < 6; l++) {
                        tfBasket[k][l] = tfBasketNew[k][l];
                    }
                }
            }
            for (int j = 0; j < 6; j++) {                       //Loob massiivi konkreetsed v‰ljad
                tfBasket[i][j] = new TextField();
                tfBasket[i][j].setPrefWidth(sceneWidth/7);
                tfBasket[i][0].setText(Integer.toString(rowCounter+1));
                tfBasket[i][0].setEditable(false);              //Rea nr on automaatne, kasutaja ei tohi seda muuta
                basketFields.add(tfBasket[i][j], j, i);         //Lisab basketFieldsile, mis on GridPane, TextFieldid
                basket.setContent(basketFields);                //lisab ostukorvile, mis on ScrollPane, GridPane, mis koosneb TextFieldidest

            }

            tfBasket[rowCounter][5].setEditable(false);         //rea summa on automaatne - kasutaja ei tohi seda muuta
            tfBasketNew = tfBasket;                             //kopeerib kasutaja poolt varasemalt sisestatud v‰‰rtuste massiivi,
        }                                                       //et oleks kust j2rgmise tfBasket objekti (kasutaja "enter" vajutamisel) loomisel v‰‰rtusi vıtta


        tfBasket[rowCounter][4].setOnAction(event -> {          //Kasutaja "enter" vajutamisel p‰rast hinna sisestamist tekitab uue rea, kutsudes end uuesti esile
            tfBasket[rowCounter][4].setOnAction(null);          //Kuidas "vıimet" eemaldada: http://stackoverflow.com/questions/14927233/programmatically-remove-a-listener-added-using-fxml
            rowCounter++;
            purchaseBasketFields();
        });

        tfBasket[rowCounter][1].textProperty().addListener(((observable1, oldValue1, newValue1) ->{ //Item: V‰‰rtuse muutumisel lisa kategooria (kui on lisada)
            getCategory();
        }));

        tfBasket[rowCounter][3].textProperty().addListener((observable, oldValue, newValue) -> {  //Quantity: V‰‰rtuse muutumisel arvuta rea summa
            calculateRowAmount();

        });

        tfBasket[rowCounter][4].textProperty().addListener((observable, oldValue, newValue) -> {  //Price: V‰‰rtuse muutumisel arvuta rea summa
            calculateRowAmount();

        });

    }

    //Meetod leiab igal real asetleidnud muutuse korral vastava rea kategooria, juhul kui selline ese on juba kord ostetud
    private void getCategory() {
        Databases db = new Databases();

        for (int i = 0; i < rowCounter + 1; i++) {
            String item = tfBasket[i][1].getText();
            String category = db.getCategoryForItem(item);
            if (db.checkItemExistance(item)) {
                tfBasket[i][2].setText(category);
                tfBasket[i][2].setEditable(false); //Kui kategooria on leitud, siis kasutaja seda enam k‰sitsi muuta ei saa
            } else {
                tfBasket[i][2].clear();
                tfBasket[i][2].setEditable(true);
            }
        }
        db.closeConnection();
    }



    //Meetod arvutab rea summa, korrutades hinna ja koguse. Seejuures kontrollitakse, kas kasutaja on sisestanud numbrilised
    //v‰‰rtused (kui mitte, siis veateade). Niipalju tuleb kasutajale vastu, et komad asendab punktiga.
    private void calculateRowAmount() {
        BigDecimal purchaseAmount = new BigDecimal(0);
        for (int i = 0; i < rowCounter + 1; i++) {

            if(tfBasket[i][3].getText().isEmpty() || tfBasket[i][4].getText().isEmpty()) { //Kui kogus vıi hind on t¸hi, t¸hjenda summa
                tfBasket[i][5].clear();
            } else {

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

                try {                                                           //Kontrollib numbrilist v‰‰rtust koguse ja hinna lahtris
                    BigDecimal quantity = new BigDecimal(tfBasket[i][3].getText());
                    BigDecimal price = new BigDecimal(tfBasket[i][4].getText());
                    BigDecimal amount = quantity.multiply(price);
                    purchaseAmount = purchaseAmount.add(amount);
                    tfBasket[i][5].setText(amount.toString());
                    tfBasket[i][3].setStyle(null);
                    tfBasket[i][4].setStyle(null);
                    alertMessage.setText(null); //Kui kasutaja tuleb selle peale, et enne parandamist sisestada uus rida, siis kaob vahepeal alert 2ra.
                } catch (java.lang.NumberFormatException e) { //Kui saadakse viga, et lahtrisse pole sisestatud numbriline v‰‰rtus: veateade
                    alertMessage.setText("Quantity or price format is incorrect: must be number");
                    alertMessage.setFill(Color.RED);
                    tfBasket[i][3].setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                    tfBasket[i][4].setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                }
            }


        }
        tfPurchaseAmount.setText(purchaseAmount.toString());

    }

    //Meetod kontrollib "Save" nupu vajutamisel, kas kıik p‰ise v‰ljad on t‰idetud ning kas kıik osturead on t‰ielikud
    //Osturidades vıivad olla kas t‰iesti t¸hjad vıi t‰iesti t‰is read.
    //Kui tagastatakse "false" siis ostu ei salvestata ja kuvatakse kasutajale veateade
    private boolean checkInsertionCorrectness () {

        if(tfBuyer.getText().isEmpty() || tfStore.getText().isEmpty() || dpDate.getValue() == null) { //Kui kuup‰ev pole valitud, siis see vırdub null: http://code.makery.ch/blog/javafx-8-date-picker/
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
                    if( (tfBasket[i][1].getText().isEmpty() && tfBasket[i][2].getText().isEmpty() && tfBasket[i][3]
                            .getText().isEmpty() && tfBasket[i][4].getText().isEmpty() && tfBasket[i][5].getText()
                            .isEmpty()) || (!tfBasket[i][1].getText().isEmpty() && !tfBasket[i][2].getText().isEmpty()
                            && !tfBasket[i][3].getText().isEmpty() && !tfBasket[i][4].getText().isEmpty() &&
                            !tfBasket[i][5].getText().isEmpty())) {

                    } else {
                        alertMessage.setText("You have uncompleted rows");
                        alertMessage.setFill(Color.RED);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //Meetod k‰sib andmebaasi objektil antud ost andmebaasi sisestada
    private void savePurchaseToDB() {

        if (checkInsertionCorrectness()) { //Kui kontroll on andnud ıige tulemuse, siis..

            for (int i = 0; i < rowCounter + 1; i++) { //Kordab seda ts¸klit niikaua, kuni on k‰idud l‰bi kıik read ja sorteerinud sealt v‰lja t‰idetud read
                if  (!tfBasket[i][1].getText().isEmpty() && !tfBasket[i][2].getText().isEmpty() && !tfBasket[i][3]
                        .getText().isEmpty() && !tfBasket[i][4].getText().isEmpty() && !tfBasket[i][5].getText()
                        .isEmpty()) {
                    Databases db = new Databases();

                    String buyer = tfBuyer.getText();
                    String date = dpDate.getValue().toString();
                    String store = tfStore.getText();

                    int basketRowNr = Integer.parseInt(tfBasket[i][0].getText());
                    String item = tfBasket[i][1].getText();
                    String category = tfBasket[i][2].getText();
                    BigDecimal quantity = new BigDecimal(tfBasket[i][3].getText());
                    BigDecimal basketRowAmount = new BigDecimal(tfBasket[i][5].getText());

                    db.registerBuyer(buyer);        //registreerib ostjate listi
                    db.savePurchaseBasket(basketRowNr, buyer, date, store, item, category, quantity, basketRowAmount); //registreerib ostu enda
                    db.registerItem(item, category); //registreerib esemete listi
                    db.closeConnection();
                }
            }
            costInputScreen.close();
            new ProgramScreen();
        }
    }

}
