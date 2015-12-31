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

    //Allj2rgneva loomisel sain abi:
    //http://www.java2s.com/Tutorials/Java/javafx.scene.chart/PieChart/0040__PieChart.PieChart_.htm
    //http://docs.oracle.com/javase/8/javafx/user-interface-tutorial/pie-chart.htm#CIHFDADD



    public PieChart amountByBuyers() {
        PieChart chart = new PieChart();
        ArrayList buyersList = db.getBuyerList();
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (int i = 0; i < buyersList.size(); i++) {
            String buyer = buyersList.get(i).toString();
            double amount = (db.getBuyerAmount(buyersList.get(i).toString())).doubleValue();
            data.addAll(new PieChart.Data(buyer,amount));
            chart.setData(data);
        }
        return chart;
    }
}

