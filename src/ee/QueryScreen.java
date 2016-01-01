package ee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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
import java.time.LocalDate;
import java.util.ArrayList;


/**
 * Created by Maila on 25/12/2015.
 */
public class QueryScreen {
    Stage queryScreen;
    BorderPane queries;
    int sceneHeight = 600;
    int sceneWidth = 1000;
    ChoiceBox cbBuyer, cbCategory;
    DatePicker dpPeriodStartDate, dpPeriodEndDate;
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
        sc.getStylesheets().add(getClass().getResource("css/test.css").toExternalForm());
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph

        queryScreen.setScene(sc);
        queryScreen.show();
    }

    private void queryOptions() {
        VBox options = new VBox();
        Label sd = new Label("Period start date");
        dpPeriodStartDate = new DatePicker();
        Label ed = new Label("Period end date");
        dpPeriodEndDate = new DatePicker();

        Label b = new Label("Buyer");
        cbBuyer = new ChoiceBox(FXCollections.observableArrayList("Avo", "Maila", "")); //Tuleb automaatseks teha

        Label c = new Label("Category");
        cbCategory = new ChoiceBox(FXCollections.observableArrayList("Toiduained", ""));

        Button executeQuery = new Button("Execute query");
        executeQuery.setOnAction(event -> {
            showQueryResult();
        });

        options.getChildren().addAll(sd, dpPeriodStartDate, ed, dpPeriodEndDate, b, cbBuyer,c, cbCategory, executeQuery);
        queries.setLeft(options);
    }

    private void showQueryResult() {
        GridPane queryResult = new GridPane();
        queryResult.setHgap(5);
        queryResult.setPrefWidth(50);

        LocalDate startDate = dpPeriodStartDate.getValue();
        LocalDate endDate = dpPeriodEndDate.getValue();

        db = new Databases();
        Tables tbl = new Tables();
        BigDecimal amount = db.getPeriodAmount(startDate, endDate);
        queries.setCenter(tbl.amountLastMonthsByCategories());
        System.out.println(amount);
        db.closeConnection();


    }

}
