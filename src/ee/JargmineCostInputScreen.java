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

        header.add(alertMessage,0,2); //Koht, kuhu saab hakata kasutajale veateateid kuvama

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
            tfBasketNew = tfBasket; //kopeerib kasutaja poolt varasemalt sisestatud v22rtused
        }

        tfBasket[rowCounter][1].setOnMouseClicked(event -> {
            tfBasket[rowCounter][1].setOnMouseClicked(null);   //Yks vastustest: http://stackoverflow.com/questions/14927233/programmatically-remove-a-listener-added-using-fxml
            rowCounter++;
            purchaseBasketFields();
        });

        tfBasket[rowCounter][3].textProperty().addListener((observable, oldValue, newValue) -> {
            calculateRowAmount(rowCounter);
        });

        tfBasket[rowCounter][4].textProperty().addListener((observable, oldValue, newValue) -> {
            calculateRowAmount(rowCounter);
        });

    }

    private void calculateRowAmount(int rowNr) { //Arvutab rea summa, korrutades hinna ja koguse
        for (int i = 0; i < rowNr + 1; i++) {

            if (!tfBasket[i][3].getText().isEmpty() && !tfBasket[i][4].getText().isEmpty()) {
                try {
                    BigDecimal quantity = new BigDecimal(tfBasket[i][3].getText());
                    BigDecimal price = new BigDecimal(tfBasket[i][4].getText());
                    BigDecimal amount = quantity.multiply(price);
                    tfBasket[i][5].setText(amount.toString());
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
    }


    private void prindiYksRida() {
        int lapsi = basketFields.getChildren().size();
        System.out.println(lapsi);
        String ridu = tfBasket[2][2].getText().toString();
        System.out.println(ridu);
    }

}
