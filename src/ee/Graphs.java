package ee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Maila on 31/12/2015.
 */
public class Graphs {



    public Graphs() { //P2ringute graafikute joonistamise jaoks

    }

    public PieChart periodAmountByBuyers(LocalDate startDate, LocalDate endDate) {
        //Pirukagraafiku loomisel sain abi:
        //http://www.java2s.com/Tutorials/Java/javafx.scene.chart/PieChart/0040__PieChart.PieChart_.htm
        //http://docs.oracle.com/javase/8/javafx/user-interface-tutorial/pie-chart.htm#CIHFDADD
        //http://docs.oracle.com/javase/8/javafx/user-interface-tutorial/line-chart.htm#CIHGBCFI

        Databases db = new Databases();
        PieChart chart = new PieChart();
        ArrayList buyersList = db.getBuyerList();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (int i = 0; i < buyersList.size(); i++) {

            String buyer = buyersList.get(i).toString();
            double amount = (db.getPeriodAmountByBuyers(buyer, startDate, endDate)).doubleValue();
            System.out.println(amount);

            data.addAll(new PieChart.Data(buyer,amount));
            chart.setData(data);
        }
        db.closeConnection(); //Kui ma kasutan siin close connectionit ja db loomise teen klassi, siis ka see tekitab j2rgmise graafikuga probleeme?
        return chart;
    }

    public PieChart periodAmountCategoryByBuyers(String category, LocalDate startDate, LocalDate endDate) {
        //Pirukagraafiku loomisel sain abi:
        //http://www.java2s.com/Tutorials/Java/javafx.scene.chart/PieChart/0040__PieChart.PieChart_.htm
        //http://docs.oracle.com/javase/8/javafx/user-interface-tutorial/pie-chart.htm#CIHFDADD
        //http://docs.oracle.com/javase/8/javafx/user-interface-tutorial/line-chart.htm#CIHGBCFI

        Databases db = new Databases();
        PieChart chart = new PieChart();
        ArrayList buyersList = db.getBuyerList();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (int i = 0; i < buyersList.size(); i++) {

            String buyer = buyersList.get(i).toString();
            double amount = (db.getPeriodAmountCategoryByBuyers(category, buyer, startDate, endDate)).doubleValue();
            System.out.println(amount);

            data.addAll(new PieChart.Data(buyer,amount));
            chart.setData(data);
        }
        db.closeConnection(); //Kui ma kasutan siin close connectionit ja db loomise teen klassi, siis ka see tekitab j2rgmise graafikuga probleeme?
        return chart;
    }

    public LineChart amountLastMonthsByCategories (int numberOfMonths) { //https://docs.oracle.com/javafx/2/charts/line-chart.htm
        Databases db = new Databases();
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();

        CategoryAxis xMonth = new CategoryAxis();
        NumberAxis yAmount = new NumberAxis();

        LineChart chart = new LineChart(xMonth , yAmount);

        ArrayList monthList = new ArrayList(); //Kuude nimekiri antakse ette kasutaja valikuga View menüüst. Sõltuvalt käesolevast kuust saab kuu oma väärtuse for tsükliga
        ArrayList categoryList = db.getCategoryList(); //Kategooriate nimekiri ja selle pikkus sõltub kasutaja sisestustest andmebaasi
        BigDecimal[][] amounts = new BigDecimal[categoryList.size()][numberOfMonths];


        XYChart.Series[] series = new XYChart.Series[categoryList.size()];

        for (int i = 0; i < numberOfMonths; i++) {
            String month = ((today.getMonth().minus(numberOfMonths-i-1)).getDisplayName(TextStyle.SHORT, Locale.ROOT));
            monthList.add(month);
        }

        for (int i = 0; i < categoryList.size(); i++) {
            series[i] = new XYChart.Series(); //Kui see rida oli j-tsüklis, siis tuli väga kole pilt
            series[i].setName(categoryList.get(i).toString());
            chart.getData().addAll(series[i]);//Kui see rida oli j-tsüklis, siis tuli illegal duplicate series added

            for (int j = 0; j < monthList.size(); j++) {
                amounts[i][j] = new BigDecimal(0);

                LocalDate startDate,endDate;

                if(j >= numberOfMonths-today.getMonthValue() ){
                    int periodMonth = currentMonth-(numberOfMonths-j-1);
                    startDate = today.withMonth(periodMonth).withDayOfMonth(1);
                    endDate = today.withMonth(periodMonth).withDayOfMonth(today.withMonth(periodMonth).lengthOfMonth());

                } else {
                    int periodMonth = 12 - (numberOfMonths-j-currentMonth-1);
                    startDate = today.withYear(today.getYear()-1).withMonth(periodMonth).withDayOfMonth(1);
                    endDate = today.withYear(today.getYear() - 1).withMonth(periodMonth).withDayOfMonth(today.withYear(today.getYear() - 1).withMonth(periodMonth).lengthOfMonth());
                }

                String category = categoryList.get(i).toString();
                amounts[i][j] = (db.getPeriodAmountByCategories(category, startDate, endDate));

                series[i].getData().add(new XYChart.Data(monthList.get(j).toString(),amounts[i][j])); //Lisab andmed graafikule

            }

        }

        db.closeConnection();
        return chart;
    }

}

