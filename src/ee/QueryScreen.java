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
        cbBuyer = new ChoiceBox(FXCollections.observableArrayList("Avo", "Maila", "")); //Tuleb automaatseks teha

        Label c = new Label("Category");
        cbCategory = new ChoiceBox(FXCollections.observableArrayList("Toiduained", ""));

        Button executeQuery = new Button("Execute query");
        executeQuery.setOnAction(event -> {
            showQueryResult();
        });

        options.getChildren().addAll(sd, pickPeriodStartDate, ed, pickPeriodEndDate, b, cbBuyer,c, cbCategory, executeQuery);
        queries.setLeft(options);
    }

    private void showQueryResult() {
        GridPane queryResult = new GridPane();
        queryResult.setHgap(5);
        queryResult.setPrefWidth(50);

        String buyer = cbBuyer.getValue().toString();
        String category = cbCategory.getValue().toString();

        System.out.println(buyer);
        db = new Databases();
        if(!buyer.isEmpty() && category.isEmpty()) {
            BigDecimal amount = db.calculateBuyerAmount(buyer);
            Text b = new Text(buyer);
            Text a = new Text(amount.toString());
            queryResult.add(b,1,1);
            queryResult.add(a,2,1);
            queries.setCenter(queryResult);
            db.closeConnection();
        }
        if(!buyer.isEmpty() && !category.isEmpty()) {
            BigDecimal amount = db.calculateCostgroupAmount(category, buyer);
            Text c = new Text(category);
            Text b = new Text(buyer);
            Text a = new Text(amount.toString());
            queryResult.add(c,1,1);
            queryResult.add(b,2,1);
            queryResult.add(a,3,1);
            queries.setCenter(queryResult);
            db.closeConnection();
        }
        if(buyer.isEmpty() && !category.isEmpty()) {
            ArrayList amountList = db.calculateCostgroupAmountByBuyers(category);
            Text c = new Text(category);
            Text b1 = new Text("Avo");
            Text a1 = new Text(amountList.get(0).toString());
            Text b2 = new Text("Maila");
            Text a2 = new Text(amountList.get(1).toString());

            queryResult.add(c,1,1);
            queryResult.add(b1,2,1);
            queryResult.add(a1,3,1);
            queryResult.add(b2,2,2);
            queryResult.add(a2,3,2);
            queries.setCenter(queryResult);
            db.closeConnection();

            ObservableList<PieChart.Data> pieChartData = //http://docs.oracle.com/javase/8/javafx/user-interface-tutorial/pie-chart.htm#CIHFDADD
                    FXCollections.observableArrayList(
                            new PieChart.Data("Avo", Double.parseDouble(amountList.get(0).toString())),
                            new PieChart.Data("Maila", Double.parseDouble(amountList.get(1).toString())));
            final PieChart chart = new PieChart(pieChartData);
            chart.setTitle(category+" by buyers");
            queryResult.add(chart, 3, 3);


        }
    }

    private String buyerList() { //Otsi andmebaasist üles, kui palju on ostjaid
        return null;
    }
}
