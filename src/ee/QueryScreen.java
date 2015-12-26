package ee;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;


/**
 * Created by Maila on 25/12/2015.
 */
public class QueryScreen {
    Stage queryScreen;
    BorderPane queries;
    int sceneHeight = 600;
    int sceneWidth = 1000;
    ChoiceBox cbBuyer;
    Databases db;


    public QueryScreen() {
        setupScene();
        queryOptions(); //Valikud, mille l6ikes p2ringuid teha
    }

    private void setupScene() {
        queries = new BorderPane();
        queryScreen = new Stage();
        queryScreen.setTitle("Queries");

        queryScreen.setOnCloseRequest(event -> {
            queryScreen.close();
            new ProgramScreen();
        });
        Scene sc = new Scene(queries, sceneWidth, sceneHeight);
        queryScreen.setScene(sc);
        queryScreen.show();
    }

    private void queryOptions() {
        VBox options = new VBox();
        Label sd = new Label("Period start date");
        DatePicker pickPeriodStartDate = new DatePicker();
        Label ed = new Label("Period end date");
        DatePicker pickPeriodEndDate = new DatePicker();

        Label b = new Label("Buyer");

        cbBuyer = new ChoiceBox(FXCollections.observableArrayList("Avo", "Maila")); //Tuleb automaatseks teha
        Label c = new Label("Category");
        ChoiceBox cbCategory = new ChoiceBox();

        Button executeQuery = new Button("Execute query");
        executeQuery.setOnAction(event -> {
            showQueryResult();
        });

        options.getChildren().addAll(sd, pickPeriodStartDate, ed, pickPeriodEndDate, b, cbBuyer,c, cbCategory, executeQuery);
        queries.setLeft(options);
    }

    private void showQueryResult() {
        GridPane queryResult = new GridPane();
        String buyer = cbBuyer.getValue().toString();
        System.out.println(buyer);
        db = new Databases();
        BigDecimal amount = db.calculateBuyerAmount(buyer);
        Text b = new Text(buyer);
        Text a = new Text(amount.toString());
        queryResult.add(b,1,1);
        queryResult.add(a,2,1);
        queries.setCenter(queryResult);
        db.closeConnection();
    }

    private String buyerList() { //Otsi andmebaasist üles, kui palju on ostjaid
        return null;
    }
}
