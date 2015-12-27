package ee;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;

/**
 * Created by Maila on 27/12/2015.
 */
public class JargmineCostInputScreen {
    int sceneHeight = 1000;
    int sceneWidth = 700;
    VBox purchase;
    int rowCounter = 1; //vajalik esimeseks loomiseks ära väärtustada
    GridPane basketLabels, basketFields;
    ScrollPane basket;
    TextField[][] tfBasket, tfBasketNew;
    int fullRows;


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
        header.getColumnConstraints().addAll(column, column, column);

        Label l1 = new Label ("Buyer");
        Label l2 = new Label ("Store");
        Label l3 = new Label ("Date");
        header.add(l1,0,0);
        header.add(l2,1,0);
        header.add(l3,2,0);

        TextField tfBuyer = new TextField();
        TextField tfStore = new TextField();
        DatePicker dpDate = new DatePicker();
        header.add(tfBuyer,0,1);
        header.add(tfStore,1,1);
        header.add(dpDate,2,1);

        Button btn1 = new Button("test1");
        header.add(btn1,3,1);

        Button btn2 = new Button("test2");
        btn2.setOnAction(event -> {
            prindiYksRida();
        });
        header.add(btn2,3,2);

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

        basketFields = new GridPane(); //Lisaks Labelitele lisab ka esimese osturea, vastasel korral hakkasid tulema duplicate childreni veateated, sest sama asja lisati mitu korda. Nyyd yhekordselt.
        basketFields.setHgap(5);
        basketFields.setVgap(5);
        basketFields.setPadding(new Insets(10, 10, 10, 10));
        basket = new ScrollPane();
        basket.setStyle("-fx-background: #00F00F");
        purchase.getChildren().add(basket);
    }



    private void purchaseBasketFields() {
        tfBasket = new TextField[rowCounter][6];

        for (int i = rowCounter-1; i < rowCounter; i++) {
            if(rowCounter > 1) {
                for (int k = 0; k < rowCounter - 1; k++) {
                    for (int l = 0; l < 6; l++) {
                        tfBasket[k][l] = tfBasketNew[k][l];
                    }
                }
            }
            for (int j = 0; j < 6; j++) {
                tfBasket[i][j] = new TextField();
                tfBasket[i][j].setPrefWidth(sceneWidth/7);
                tfBasket[i][0].setText(Integer.toString(rowCounter));
                tfBasket[i][0].setEditable(false);
                basketFields.add(tfBasket[i][j], j, i);
                basket.setContent(basketFields); //lisab ostukorvile, mis on ScrollPane, textFieldid
            }
            tfBasketNew = tfBasket;
        }

        tfBasket[rowCounter-1][1].setOnMouseClicked(event -> {
            rowCounter = rowCounter + 1;
            purchaseBasketFields();
        });

        calculateRowAmount();
    }

    private void calculateRowAmount() { //Arvutab rea summa, korrutades hinna ja koguse
        for (int i = 0; i < rowCounter; i++) {
            tfBasket[i][4].setOnInputMethodTextChanged(event -> {
                TextField pr = (TextField) event.getTarget(); //loogika laevadefx-st
                Integer row = GridPane.getRowIndex(pr);
                BigDecimal quantity = new BigDecimal(tfBasket[row][3].getText());
                BigDecimal price = new BigDecimal(tfBasket[row][4].getText());
                BigDecimal amount = quantity.multiply(price);
                tfBasket[row][5].setText(amount.toString());
            });
        }
    }

    private void prindiYksRida() {
        int lapsi = basketFields.getChildren().size();
        System.out.println(lapsi);
        String ridu = tfBasket[2][2].getText().toString();
        System.out.println(ridu);
    }

}
