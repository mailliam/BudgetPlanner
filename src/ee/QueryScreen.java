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
import javafx.stage.Stage;

/**
 * Created by Maila on 25/12/2015.
 */
public class QueryScreen {
    Stage queryScreen;
    BorderPane queries;
    int sceneHeight = 600;
    int sceneWidth = 1000;


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
        ChoiceBox cbBuyer = new ChoiceBox(FXCollections.observableArrayList("Ostja 1", "Ostja 2"));
        Label c = new Label("Category");
        ChoiceBox cbCategory = new ChoiceBox();

        Button executeQuery = new Button("Execute query");

        options.getChildren().addAll(sd, pickPeriodStartDate, ed, pickPeriodEndDate, b, cbBuyer,c, cbCategory, executeQuery);
        queries.setLeft(options);
    }

    private String buyerList() { //Otsi andmebaasist üles, kui palju on ostjaid
        return null;
    }
}
