package ee;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdk.internal.util.xml.impl.Input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Maila on 25/11/2015.
 */
public class InputScreenVana {
    //Siia tuleb kulude sisestamine

    Stage inputScreen = new Stage();
    int sceneHeight = 1000;
    int sceneWidth = 600;
    int buttonWidth = 150;
    int labelWidth = 100;
    int textFieldWidth = 100;
    double amount;
    TextField fieldBuyer, fieldStore, fieldDate;
    TextField[] fieldRow_id,fieldItem,fieldCostgroup,fieldQuantity,fieldPrice,fieldAmount;
    int numberOfRows = 20;
    int rowCounter=1;
    GridPane basket;



    Button cancel,save;

    public InputScreenVana() {
        setupScene();
        calculateRowAmount();
        savePurchase();

    }

    private void setupScene() { //Kulude sisestuse akna seadistus
        inputScreen.setTitle("Cost Input");
        inputScreen.setOnCloseRequest(event ->{
            inputScreen.close();
            new ProgramScreen();
        });

        VBox purchaseTotal = new VBox();
        Label labelBuyer = new Label("Buyer");
        labelBuyer.setPrefWidth(labelWidth);

        fieldBuyer = new TextField();
        fieldBuyer.setPrefWidth(textFieldWidth);

        Label labelStore = new Label("Store");
        labelStore.setPrefWidth(labelWidth);

        fieldStore = new TextField();
        fieldStore.setPrefWidth(textFieldWidth);

        Label labelDate = new Label("Date");
        labelDate.setPrefWidth(labelWidth);

        final DatePicker fieldDate = new DatePicker(); //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DatePicker.html
        fieldDate.setOnAction(event -> {
            LocalDate date = fieldDate.getValue();
            System.err.println("Selected date: " +date); //Seda pole tegelikult vaja
        });

        fieldDate.setPrefWidth(textFieldWidth+50); //kuupäev ei mahu muidu ära

        HBox purchaseHeaderLabels = new HBox();
        purchaseHeaderLabels.setSpacing(5);
        purchaseHeaderLabels.getChildren().addAll(labelBuyer, labelStore, labelDate);
        HBox purchaseHeaderFields = new HBox();
        purchaseHeaderFields.setSpacing(5);
        purchaseHeaderFields.getChildren().addAll(fieldBuyer, fieldStore, fieldDate);

        Label labelRow_id = new Label("Row nr.");
        labelRow_id.setPrefWidth(labelWidth);

        Label labelItem = new Label("Item");
        labelItem.setPrefWidth(labelWidth);

        Label labelCostgroup = new Label("Costgroup");
        labelCostgroup.setPrefWidth(labelWidth);

        Label labelQuantity = new Label("Quantity");
        labelQuantity.setPrefWidth(labelWidth);

        Label labelPrice = new Label("Price");
        labelPrice.setPrefWidth(labelWidth);

        Label labelAmount = new Label("Amount");
        labelAmount.setPrefWidth(labelWidth);

        HBox purchaseContentLabels = new HBox();
        purchaseContentLabels.setPadding(new Insets(10, 0, 10, 0));
        purchaseContentLabels.getChildren().addAll(labelRow_id,labelItem,labelCostgroup,labelQuantity,labelPrice, labelAmount);

        basket = new GridPane();
        basket.setHgap(5);
        basket.setVgap(5);
        fieldRow_id = new TextField[numberOfRows];
        fieldItem = new TextField[numberOfRows];
        fieldCostgroup = new TextField[numberOfRows];
        fieldQuantity = new TextField[numberOfRows];
        fieldPrice = new TextField[numberOfRows];
        fieldAmount = new TextField[numberOfRows];

        for (int i = 0; i < numberOfRows; i++) { //http://www.javafxapps.in/tutorial/Grid-Pane-layout-Example.html
            fieldRow_id[i] = new TextField();
            fieldRow_id[i].setPrefWidth(textFieldWidth);
            fieldItem[i] = new TextField();
            fieldItem[i].setPrefWidth(textFieldWidth);
            fieldCostgroup[i] = new TextField();
            fieldCostgroup[i].setPrefWidth(textFieldWidth);
            fieldQuantity[i] = new TextField();
            fieldQuantity[i].setPrefWidth(textFieldWidth);
            fieldPrice[i] = new TextField();
            fieldPrice[i].setPrefWidth(textFieldWidth);
            fieldAmount[i] = new TextField();
            fieldAmount[i].setPrefWidth(textFieldWidth);
            basket.add(fieldRow_id[i],1,i);
            basket.add(fieldItem[i],2,i);
            basket.add(fieldCostgroup[i],3,i);
            basket.add(fieldQuantity[i], 4, i);
            basket.add(fieldPrice[i], 5, i);
            basket.add(fieldAmount[i], 6, i);
        }

        save = new Button("Save purchase");
        save.setAlignment(Pos.BOTTOM_RIGHT);

        purchaseTotal.getChildren().addAll(purchaseHeaderLabels, purchaseHeaderFields, purchaseContentLabels, basket, save);
        Scene sc = new Scene(purchaseTotal, sceneWidth, sceneHeight);
        inputScreen.setScene(sc);
        inputScreen.show();
    }

    private void calculateRowAmount() { //Arvutab rea summa, korrutades hinna ja koguse
        for (int i = 0; i <numberOfRows; i++) {
            fieldPrice[i].setOnAction(event -> {
                TextField pr = (TextField) event.getTarget(); //loogika laevadefx-st
                Integer row = GridPane.getRowIndex(pr);
                double quantity = Double.parseDouble(fieldQuantity[row].getText());
                double price = Double.parseDouble(fieldPrice[row].getText());
                amount = quantity * price;
                fieldAmount[row].setText(Double.toString(amount));
            });
        }
    }

    private void savePurchase() { //Salvestab ostu andmed ostude baasi. Kas võib kasutada sama meetodi nime, mis on database juures?
        save.setOnAction(event -> {
           Databases dbPurchase = new Databases();
            for (int i = 0; i < numberOfRows; i++) {
                String buyer = fieldBuyer.getText();
                int date = Integer.parseInt(fieldDate.getText()); //tegelikult selle peab vist kuidagi �ra formattima
                String store = fieldStore.getText();
                int purchaseRowID = Integer.parseInt(fieldRow_id[i].getText());
                String item = fieldItem[i].getText();
                String costgroup = fieldCostgroup[i].getText();
                double quantity = Double.parseDouble(fieldQuantity[i].getText());
                double price = Double.parseDouble(fieldPrice[i].getText());

                dbPurchase.savePurchase(buyer, date, store, purchaseRowID, item, costgroup, quantity, price);

            }
            dbPurchase.closeConnection();
            inputScreen.close();
            new ProgramScreen();

        });

    }
}




