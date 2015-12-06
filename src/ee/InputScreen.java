package ee;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jdk.internal.util.xml.impl.Input;

/**
 * Created by Maila on 25/11/2015.
 */
public class InputScreen {
    //Siia tuleb kulude sisestamine

    Stage inputScreen = new Stage();
    int sceneHeight = 400;
    int sceneWidth = 600;
    int buttonWidth = 100;
    Button cancel,save;

    public InputScreen() {
        setupScene();
    }

    private void setupScene() {
        inputScreen.setTitle("Cost Input");
        inputScreen.setOnCloseRequest(event ->{
            inputScreen.close();
            new ProgramScreen();
        });
        VBox purchaseTotal = new VBox();
        Label labelBuyer = new Label("Buyer");
        TextField fieldBuyer = new TextField();
        Label labelStore = new Label("Store");
        TextField fieldStore = new TextField();
        Label labelDate = new Label("Date");
        TextField fieldDate = new TextField();
        HBox purchaseHeader = new HBox();
        purchaseHeader.getChildren().addAll(labelBuyer, fieldBuyer, labelStore, fieldStore, labelDate, fieldDate);

        Label labelItem = new Label("Item");
        TextField fieldItem = new TextField();
        Label labelCostgroup = new Label("Costgroup");
        TextField fieldCostgroup = new TextField();
        Label labelQuantity = new Label("Quantity");
        TextField fieldQuantity = new TextField();
        Label labelPrice = new Label("Price");
        TextField fieldPrice = new TextField();
        HBox purchaseContent = new HBox();
        purchaseContent.getChildren().addAll(labelItem,fieldItem,labelCostgroup,fieldCostgroup,labelQuantity,fieldQuantity,labelPrice,fieldPrice);

        purchaseTotal.getChildren().addAll(purchaseHeader,purchaseContent);
        Scene sc = new Scene(purchaseTotal);
        inputScreen.setScene(sc);
        inputScreen.show();
    }


}
