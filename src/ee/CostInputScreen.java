package ee;

import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;

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
    TextField fieldBuyer, fieldStore;
    DatePicker fieldDate;
    HBox h1,h2;

    public CostInputScreen() {
        setupScene();
        purchaseHeaderLabels();
        purchaseHeaderFields();

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
        h1 = new HBox();
        Label[][] headerLabels = new Label[1][3];
        for (int i = 0; i < 3; i++) {
            headerLabels[0][i] = new Label();
            headerLabels[0][i].setPrefWidth(width);
            h1.getChildren().add(headerLabels[0][i]);
        }
        headerLabels[0][0].setText("Buyer");
        headerLabels[0][1].setText("Store");
        headerLabels[0][2].setText("Date");

        purchaseTotal.add(h1,1,1);

    }

    private void purchaseHeaderFields() {
        h2 = new HBox();

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
        h2.getChildren().addAll(fieldBuyer, fieldStore, fieldDate);
        purchaseTotal.add(h2,1,2);


    }

}
