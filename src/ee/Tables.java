package ee;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.Month;

import java.time.temporal.ChronoField;

import java.util.ArrayList;


/**
 * Created by Maila on 31/12/2015.
 */
public class Tables { //P2ringu tabelite jaoks

    public Tables() {

    }

    public GridPane amountLastMonthsByCategories() { //Arvutab kolme eelmise kuu ostud ja paneb need tabelisse (teoorias)
        Databases db = new Databases();
        ArrayList categoryList = db.getCategoryList();
        GridPane table = new GridPane();
        table.setGridLinesVisible(true);
        table.setHgap(2);
        table.setVgap(2);

        int numberOfMonths = 6; //Periood, mitu kuud tagasi vaadatakse

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
            categories[i][0].setFont(Font.font("Arial", FontWeight.BOLD, 12));
            table.add(categories[i][0], 0, i+1);
        }

        for (int j = 0; j < numberOfMonths; j++) {
            months[0][j] = new Label();
            months[0][j].setText((today.getMonth().minus(numberOfMonths-j-1)).toString());
            months[0][j].setFont(Font.font("Arial", FontWeight.BOLD, 20));
            table.add(months[0][j],j+1,0);


        }

        for (int i = 0; i < categoryList.size(); i++) {
            for (int j = 0; j < numberOfMonths; j++) {
                amounts[i][j] = new Label();
                LocalDate startDate,endDate;
                int currentMonth = today.getMonthValue();

                if(currentMonth > numberOfMonths-1) {
                    startDate = today.withMonth(today.getMonthValue()-(numberOfMonths-j)).withDayOfMonth(1);
                    endDate = today.withMonth(today.getMonthValue()-(numberOfMonths-j)).withDayOfMonth(today.withMonth(today.getMonthValue()-(numberOfMonths-j)).lengthOfMonth());

                } else {
                    startDate = today.withYear(today.getYear()-1).withMonth(12-(numberOfMonths-j-2)).withDayOfMonth(1);
                    endDate = today.withYear(today.getYear()-1).withMonth(12-(numberOfMonths-j-2)).withDayOfMonth(today.withYear(today.getYear()-1).withMonth(12-(numberOfMonths-j-2)).lengthOfMonth());
                }

                String category = categoryList.get(i).toString();
                amounts[i][j].setText(db.getPeriodAmountByCategories(category, startDate, endDate).toString());
                table.add(amounts[i][j],j+1,i+1);
            }
        }
        db.closeConnection();
        return table;
    }

    public void kuup2evaKatsetus() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        LocalDate prevMonth;
        if (month > 2) {
            prevMonth = today.withMonth(today.getMonthValue()-1);

        } else {
            prevMonth = today.withMonth(12-1).withYear(today.getYear()-1);
        }
        System.out.println(prevMonth);

    }

}
