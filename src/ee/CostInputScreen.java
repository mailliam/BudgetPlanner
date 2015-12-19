package ee;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

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
    TextField[][] basketFields;
    GridPane basket;
    Button save;

    public CostInputScreen() {
        setupScene();
        purchaseHeaderLabels();
        purchaseHeaderFields();
        purchaseContentsLabels();
        purchaseContentsFields();
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
        basket = new GridPane();
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
        save.setOnAction(event1 -> {
            savePurchase();
        });

        HBox h = new HBox();

        fieldBuyer = new TextField();
        fieldBuyer.setPrefWidth(width);
        fieldStore = new TextField();
        fieldStore.setPrefWidth(width);

        fieldDate = new DatePicker(); //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DatePicker.html
        fieldDate.setOnAction(event -> {
            LocalDate date = fieldDate.getValue();
            System.err.println("Selected date: " +date); //Seda pole tegelikult vaja
        });

        fieldDate.setPrefWidth(width + 50); //kuupäev ei mahu muidu ära
        h.getChildren().addAll(fieldBuyer, fieldStore, fieldDate,save);
        purchaseTotal.add(h,1,2);
    }

    private void purchaseContentsLabels() {
        HBox h = new HBox();
        Label[] basketLabels = new Label[6];
        for (int i = 0; i < 6; i++) {
            basketLabels[i] = new Label();
            basketLabels[i].setPrefWidth(width);
            h.getChildren().add(basketLabels[i]);
        }
        basketLabels[0].setText("Row nr.");
        basketLabels[1].setText("Item");
        basketLabels[2].setText("Costgroup");
        basketLabels[3].setText("Quantity");
        basketLabels[4].setText("Price");
        basketLabels[5].setText("Amount");
        purchaseTotal.add(h,1,3);
    }

    private void purchaseContentsFields() {
        basket = new GridPane();
        rowCounter = 0;
        basketFields = new TextField[rowCounter+1][6];
        for (int i = 0; i < 6; i++) {
            basketFields[rowCounter][i] = new TextField();
            basket.add(basketFields[rowCounter][i],i,rowCounter+1);
        }
        purchaseTotal.add(basket,1,rowCounter+4);
        basketFields[rowCounter][1].setOnMouseClicked(event -> {
            insertNewRow();
        });
    }

    private void insertNewRow() {
        rowCounter = rowCounter+1;
        for (int i = 0; i < 6; i++) {
            basketFields[0][i] = new TextField();
            basket.add(basketFields[0][i], i, rowCounter+1);
            basketFields[0][1].setOnMouseClicked(event -> {
                insertNewRow();
            });
        }
    }

    private void savePurchase() {
        ArrayList[] purchaseRow = new ArrayList[8];

        for (int i = 0; i < rowCounter; i++) {
            String buyer = fieldBuyer.getText();
            System.out.println("Ostja on: " +buyer);
            String store = fieldStore.getText();
            String item = basketFields[i][1].getText();
            String costgroup = basketFields[i][2].getText();
            String quantity = basketFields[i][3].getText();
            System.out.println("Kogus on: " +quantity);
            purchaseRow[i].add(buyer);
            purchaseRow[i].add(store);
            purchaseRow[i].add(item);
            purchaseRow[i].add(costgroup);
            purchaseRow[i].add(quantity);
            System.out.println("Osturida = " + purchaseRow[i]);

        }
        System.out.println("Salvestan");
        System.out.println(rowCounter);

    }
}

