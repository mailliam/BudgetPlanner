package ee;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.Month;

import java.time.format.TextStyle;
import java.time.temporal.ChronoField;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by Maila on 31/12/2015.
 */
public class Tables { //P2ringu tabelite jaoks

    public Tables() {

    }

    public GridPane amountLastMonthsByCategories(int numberOfMonths) { //Arvutab kolme eelmise kuu ostud ja paneb need tabelisse (teoorias)
        Databases db = new Databases();
        ArrayList categoryList = db.getCategoryList();
        GridPane table = new GridPane();
        table.setGridLinesVisible(true);

        ColumnConstraints[] column = new ColumnConstraints[numberOfMonths]; //http://docs.oracle.com/javafx/2/layout/size_align.htm


        //numberOfMonths = 6 //Periood, mitu kuud tagasi vaadatakse

        LocalDate today = LocalDate.now(); //http://www.java2s.com/Tutorials/Java/java.time/LocalDate/index.htm
        Month this_Month = today.getMonth(); //Kas kuup2evade jaoks v6iks ka eraldi klassi teha?
        Month prev_1_Month = today.getMonth().minus(1);
        Month prev_2_Month = today.getMonth().minus(2);
        //LocalDate startDate = today.withDayOfMonth(1);
        //LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());

        Label[][] months, categories, amounts;

        categories = new Label[categoryList.size()][1];
        months = new Label[1][numberOfMonths];

        amounts = new Label[categoryList.size()][numberOfMonths];

        for (int i = 0; i < categoryList.size(); i++) {
            categories[i][0] = new Label();
            categories[i][0].setText((categoryList.get(i)).toString());
            categories[i][0].setFont(Font.font("Arial", FontWeight.BOLD, 20));
            table.add(categories[i][0], 0, i+1);
        }

        for (int j = 0; j < numberOfMonths; j++) {
            column[j] = new ColumnConstraints();
            months[0][j] = new Label();
            column[j] = new ColumnConstraints();
            months[0][j].setText((today.getMonth().minus(numberOfMonths-j-1)).getDisplayName(TextStyle.SHORT, Locale.ROOT));
            months[0][j].setFont(Font.font("Arial", FontWeight.BOLD, 20));
            table.add(months[0][j],j+1,0);
            column[j].setPrefWidth(100);
            table.getColumnConstraints().add(j,column[j]);

        }

        for (int i = 0; i < categoryList.size(); i++) {
            int currentMonth = today.getMonthValue();
            for (int j = 0; j < numberOfMonths; j++) {

                amounts[i][j] = new Label();

                LocalDate startDate,endDate;

                if (currentMonth > (numberOfMonths-1) ) {
                    int periodMonth = currentMonth-j;
                    startDate = today.withMonth(periodMonth).withDayOfMonth(1);
                    endDate = today.withMonth(periodMonth).withDayOfMonth(today.withMonth(periodMonth).lengthOfMonth());

                } else {
                    if(j >= numberOfMonths-today.getMonthValue() ){
                        int periodMonth = currentMonth-(numberOfMonths-j-1);
                        startDate = today.withMonth(periodMonth).withDayOfMonth(1);
                        endDate = today.withMonth(periodMonth).withDayOfMonth(today.withMonth(periodMonth).lengthOfMonth());

                    } else {
                        int periodMonth = 12 - (numberOfMonths-j-currentMonth-1);
                        startDate = today.withYear(today.getYear()-1).withMonth(periodMonth).withDayOfMonth(1);
                        endDate = today.withYear(today.getYear() - 1).withMonth(periodMonth).withDayOfMonth(today.withYear(today.getYear() - 1).withMonth(periodMonth).lengthOfMonth());
                    }
                }

                String category = categoryList.get(i).toString();
                amounts[i][j].setText(db.getPeriodAmountByCategories(category, startDate, endDate).toString());
                table.add(amounts[i][j], j + 1, i + 1);

            }
        }
        db.closeConnection();
        return table;
    }

    public void kuup2evaKatsetus() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        String prevMonth = today.getMonth().getDisplayName(TextStyle.SHORT, Locale.ROOT);
        System.out.println(prevMonth);



    }

}
