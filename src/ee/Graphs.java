package ee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Maila on 31/12/2015.
 */
public class Graphs {

    Databases db = new Databases();

    public Graphs() { //Graafikute joonistamise jaoks
    }

    public Node amountByBuyers() {
        ArrayList buyersList = db.getBuyerList();
        PieChart chart;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (int i = 0; i < buyersList.size(); i++) {

            String buyer = buyersList.get(i).toString();
            double amount = (db.getBuyerAmount(buyersList.get(i).toString())).doubleValue();

            System.out.println(buyer);
            System.out.println(amount);

            pieChartData = //http://docs.oracle.com/javase/8/javafx/user-interface-tutorial/pie-chart.htm#CIHFDADD
                    FXCollections.observableArrayList(
                            new PieChart.Data(buyer,amount));
            

        }
        chart = new PieChart(pieChartData);
        chart.setTitle("Amount by buyers");
        return chart;
    }
}

