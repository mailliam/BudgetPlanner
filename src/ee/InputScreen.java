package ee;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdk.internal.util.xml.impl.Input;

/**
 * Created by Maila on 25/11/2015.
 */
public class InputScreen {
    //Siia tuleb kulude sisestamine

    Stage inputScreen = new Stage();
    int sceneHeight = 1000;
    int sceneWidth = 600;
    int buttonWidth = 100;
    int labelWidth =100;
    int textFieldWidth = 100;
    int amount;
    TextField fieldQuantity,fieldPrice,fieldAmount;

    Button cancel,save;

    public InputScreen() {
        setupScene();
        calculateRowAmount();
        purchaseInsertion();
    }


    private void setupScene() {
        inputScreen.setTitle("Cost Input");
        inputScreen.setOnCloseRequest(event ->{
            inputScreen.close();
            new ProgramScreen();
        });

        VBox purchaseTotal = new VBox();
        Label labelBuyer = new Label("Buyer");
        labelBuyer.setPrefWidth(labelWidth);

        TextField fieldBuyer = new TextField();
        fieldBuyer.setPrefWidth(textFieldWidth);

        Label labelStore = new Label("Store");
        labelStore.setPrefWidth(labelWidth);

        TextField fieldStore = new TextField();
        fieldStore.setPrefWidth(textFieldWidth);

        Label labelDate = new Label("Date");
        labelDate.setPrefWidth(labelWidth);

        TextField fieldDate = new TextField();
        fieldDate.setPrefWidth(textFieldWidth);

        HBox purchaseHeaderLabels = new HBox();
        purchaseHeaderLabels.getChildren().addAll(labelBuyer, labelStore, labelDate);
        HBox purchaseHeaderFields = new HBox();
        purchaseHeaderFields.getChildren().addAll(fieldBuyer, fieldStore, fieldDate);

        Label labelRow_id = new Label("Row nr.");
        labelRow_id.setPrefWidth(labelWidth);

        TextField fieldRow_id = new TextField();
        fieldRow_id.setPrefWidth(textFieldWidth);

        Label labelItem = new Label("Item");
        labelItem.setPrefWidth(labelWidth);

        TextField fieldItem = new TextField();
        fieldItem.setPrefWidth(textFieldWidth);

        Label labelCostgroup = new Label("Costgroup");
        labelCostgroup.setPrefWidth(labelWidth);

        TextField fieldCostgroup = new TextField();
        fieldCostgroup.setPrefWidth(textFieldWidth);

        Label labelQuantity = new Label("Quantity");
        labelQuantity.setPrefWidth(labelWidth);

        fieldQuantity = new TextField();
        fieldQuantity.setPrefWidth(textFieldWidth);

        Label labelPrice = new Label("Price");
        labelPrice.setPrefWidth(labelWidth);

        Label labelAmount = new Label("Amount");
        labelAmount.setPrefWidth(labelWidth);

        fieldPrice = new TextField();
        fieldPrice.setPrefWidth(textFieldWidth);

        fieldAmount = new TextField();
        fieldAmount.setPrefWidth(textFieldWidth);

        HBox purchaseContentLabels = new HBox();
        purchaseContentLabels.getChildren().addAll(labelRow_id,labelItem,labelCostgroup,labelQuantity,labelPrice, labelAmount);
        HBox purchaseContentFields = new HBox();
        purchaseContentFields.getChildren().addAll(fieldRow_id, fieldItem,fieldCostgroup,fieldQuantity,fieldPrice, fieldAmount);

        purchaseTotal.getChildren().addAll(purchaseHeaderLabels, purchaseHeaderFields,purchaseContentLabels,purchaseContentFields);
        Scene sc = new Scene(purchaseTotal, sceneWidth, sceneHeight);
        inputScreen.setScene(sc);
        inputScreen.show();
    }

    private int calculateRowAmount() {
        int quantity = Integer.parseInt(fieldQuantity.getText()); //http://stackoverflow.com/questions/15314205/using-gettext-to-get-an-integer
        int price = Integer.parseInt(fieldPrice.getText());  //http://stackoverflow.com/questions/15314205/using-gettext-to-get-an-integer
        amount=quantity*price;
        return amount;
    }

    private void purchaseInsertion() { //Nimi pole just hea
        fieldPrice.setOnAction(event -> {
            fieldAmount.setText(Integer.toString(amount));

        });

    }

}
