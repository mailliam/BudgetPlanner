package ee;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * Created by Maila on 25/12/2015.
 *
 * Aken, kus kasutaja saab oma ostude kohta detailsemaid päringuid teha
 *
 */
public class QueryScreen {
    Stage queryScreen;
    BorderPane queries;
    int sceneHeight = 600;
    int sceneWidth = 1000;
    int nodeWidth = sceneWidth/5;
    ChoiceBox cbCategory;
    DatePicker dpPeriodStartDate, dpPeriodEndDate;
    Databases db;
    GridPane queryResult;
    TextArea area;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd, uuuu");  //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html

    public QueryScreen() {
        setupScene();
        queryHeaderAndInstruction();
        queryOptions();
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

    private void queryHeaderAndInstruction() {
        Text heading = new Text("Queries");
        heading.setTextAlignment(TextAlignment.CENTER);
        heading.setFont(Font.font("Calibri", 30));
        queries.setTop(heading);
        queries.setAlignment(heading,Pos.TOP_CENTER);

        area = new TextArea();  //http://www.java2s.com/Code/Java/JavaFX/UsingTextAreatodisplaymorelines.htm
        String instruction = "Choose period start and end dates and/or category.\n **Result when only period is chosen:\n" +
                "Costs of different categories (table), costs by buyers (graph)\n **Result when both period and category are chosen:\n" +
                "Detailed costs of selected category (table), costs by buyers of selected category (graph)";
        area.setText(instruction);
        area.setEditable(false);
        //Text-area background css: http://stackoverflow.com/questions/21936585/transparent-background-of-a-textarea-in-javafx-8
        area.setPadding(new Insets(10));
        queries.setCenter(area);
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
        options.setPadding(new Insets(10));
        queries.setLeft(options);
    }

    private void showQueryResult() {

        queryResult = new GridPane();
        queryResult.setPadding(new Insets(10));

        if (dpPeriodStartDate.getValue()==null || dpPeriodEndDate.getValue() == null) {
            String alert = "Please choose period start and end dates.";
            area.setText(alert);
        } else {
            if(dpPeriodStartDate.getValue().isAfter(dpPeriodEndDate.getValue())||dpPeriodStartDate.getValue().isAfter(LocalDate.now())) {
                String alert = "Period start date can not be in the future or after period end date";
                area.setText(alert);
            } else {
                try {
                    queries.setCenter(periodAndCategoryResult());
                } catch (java.lang.NullPointerException e) {
                    queries.setCenter(periodResult());
                }
            }
        }
    }

    //Tulemus, kui kasutaja valis ainult periood - tabelis summad kategooriate kaupa, graafikul ostjate kaupa
    private GridPane periodResult () {

        LocalDate startDate = dpPeriodStartDate.getValue();
        LocalDate endDate = dpPeriodEndDate.getValue();

        Text heading = new Text("Costs from " +startDate.format(format)+ " to " +endDate.format(format));
        heading.setFont(Font.font("Calibri", 24));

        Tables tbl = new Tables();
        queryResult.add(heading,0,0);
        queryResult.add(tbl.periodAmountByCategories(startDate, endDate),0,1);

        Graphs graph = new Graphs();
        queryResult.add(graph.periodAmountByBuyers(startDate, endDate),1,1);
        return queryResult;
    }

    //Tulemus, kui kasutaja valis perioodi ja kategooria - tabelis kategooria summad esemete kaupa, graafikul ostjate kaupa
    private GridPane periodAndCategoryResult () {

        LocalDate startDate = dpPeriodStartDate.getValue();
        LocalDate endDate = dpPeriodEndDate.getValue();
        String category = cbCategory.getValue().toString();

        Text heading = new Text(category+ " costs from " +startDate.format(format)+ " to " +endDate.format(format));
        heading.setFont(Font.font("Calibri", 24));

        Tables tbl = new Tables();
        queryResult.add(heading,0,0);
        queryResult.add(tbl.periodAmountByItemsInCategory(category, startDate, endDate),0,1);

        Graphs graph = new Graphs();
        queryResult.add(graph.periodAmountCategoryByBuyers(category, startDate, endDate),1,1);
        return queryResult;
    }

}
