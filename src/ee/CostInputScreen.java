package ee;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    TextField[] basketFields;
    GridPane basket = new GridPane();
    Button save;

    public CostInputScreen() {
        setupScene();
        purchaseHeaderLabels();
        purchaseHeaderFields();
        purchaseContentsLabels();
        purchaseContentsFields();
        purchaseTotal.add(basket,1,3);

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
        basketLabels[5].setText("Amount");
    }

    private void purchaseContentsFields() {
        basketFields = new TextField[6];

        for (int i = 0; i < 6; i++) {
            basketFields[i] = new TextField();
            basketFields[0].setText(Integer.toString(rowCounter + 1));
            basket.add(basketFields[i], i, rowCounter + 2);

        }
        basket.getChildren().get(basket.getChildren().size()-5).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override

            public void handle(MouseEvent event) {
                if ((basket.getChildren().size() / 6 - 1) == rowCounter + 1) {
                    rowCounter++;
                    purchaseContentsFields();
                }
            }
        });
    }

    private void savePurchase() {
        ArrayList[] purchaseRow = new ArrayList[8];

        for (int i = 0; i < rowCounter; i++) {
            String buyer = fieldBuyer.getText();
            System.out.println("Ostja on: " +buyer);
            String store = fieldStore.getText();
            String item = ((TextField) basket.getChildren().get(7)).getCharacters().toString();
            System.out.println("Ese on: " +item);
            purchaseRow[i].add(buyer);
            purchaseRow[i].add(store);
            purchaseRow[i].add(item);

            System.out.println("Osturida = " + purchaseRow[i]);

        }
        System.out.println("Salvestan");
        System.out.println(rowCounter);

    }
}

