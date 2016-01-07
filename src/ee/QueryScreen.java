package ee;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.text.Font;
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
    int nodeWidth = sceneWidth/6;
    ChoiceBox cbCategory;
    DatePicker dpPeriodStartDate, dpPeriodEndDate;
    Databases db;
    GridPane queryResult;


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
        sc.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph

        queryScreen.setScene(sc);
        queryScreen.show();
    }

    private void queryOptions() {
        VBox options = new VBox();
        options.setSpacing(10);
        options.setPrefWidth(nodeWidth);

        Label sd = new Label("Period start date");
        dpPeriodStartDate = new DatePicker();
        Label ed = new Label("Period end date");
        dpPeriodEndDate = new DatePicker();

        Label b = new Label("Buyer");
        db = new Databases();
        ArrayList buyerList = db.getBuyerList();
        ArrayList categoryList = db.getCategoryList();

        Label c = new Label("Category");
        cbCategory = new ChoiceBox(FXCollections.observableArrayList(categoryList)); //Choicebox: http://www.java2s.com/Tutorials/Java/JavaFX/0450__JavaFX_ChoiceBox.htm
        cbCategory.setPrefWidth(nodeWidth);

        Button executeQuery = new Button("Execute query");
        executeQuery.setPrefWidth(nodeWidth);
        executeQuery.setOnAction(event -> {
            showQueryResult();
        });

        options.getChildren().addAll(sd, dpPeriodStartDate, ed, dpPeriodEndDate, c, cbCategory, executeQuery);
        queries.setLeft(options);
    }

    private void showQueryResult() {

        queryResult = new GridPane();
        queryResult.setHgap(5);
        queries.setCenter(periodResult());

    }

    private GridPane periodResult () {

        LocalDate startDate = dpPeriodStartDate.getValue();
        LocalDate endDate = dpPeriodEndDate.getValue();

        Text heading = new Text("Costs from " +startDate+ " to " +endDate);
        heading.setFont(Font.font("Calibri", 20));

        Tables tbl = new Tables();
        queryResult.add(heading,0,0);
        queryResult.add(tbl.amountByPeriodByCategories(startDate, endDate),0,1);

        Graphs graph = new Graphs();
        queryResult.add(graph.amountByPeriodByBuyers(startDate, endDate),1,1);
        return queryResult;
    }


}
