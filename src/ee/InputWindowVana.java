package ee;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Maila on 14/11/2015.
 */
public class InputWindowVana {
    static int windowHeight = 1000;
    static int windowWidth = 1000;
    static int buttonWidth=200;
    static int buttonHeight=50;
    static TextField item;
    static TextField costGroup;
    static TextField quantity;
    static TextField price;

    public static void inputWindow() {
        Stage windowInput = new Stage();
        windowInput.setResizable(false);
        windowInput.initModality(Modality.APPLICATION_MODAL);
        windowInput.setTitle("Cost Input");
        windowInput.setHeight(windowHeight);
        windowInput.setWidth(windowWidth);

        Label itemLabel = new Label("Item");
        Label costGroupLabel = new Label("Cost Group");
        Label quantityLabel = new Label("Quantity");
        Label priceLabel = new Label("Price");

        newLine();

        TilePane layout = new TilePane();
        layout.setHgap(4);
        layout.setPrefColumns(4);

        Button save = new Button ("Save and close");
        save.setOnAction(event -> windowInput.close());
        save.setPrefWidth(buttonWidth);
        save.setPrefHeight(buttonHeight);

        layout.getChildren().addAll(itemLabel, costGroupLabel, quantityLabel, priceLabel, item, costGroup, quantity, price, save);

        Scene scene = new Scene(layout);
        windowInput.setScene(scene);
        windowInput.showAndWait();
    }

    public static void newLine() {
        item = new TextField();
        costGroup = new TextField();
        quantity = new TextField();
        price = new TextField();
    }

}
