package ee;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

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

    //Arvutab etteantud eelmiste kuude ostud kasutades Databases objekti meetodeid ja paneb need tabelisse
    public GridPane amountLastMonthsByCategories(int numberOfMonths) {
        Databases db = new Databases();
        ArrayList categoryList = db.getCategoryList();
        GridPane table = new GridPane();
        table.setGridLinesVisible(true);

        //numberOfMonths = 6 //Periood, mitu kuud tagasi vaadatakse

        LocalDate today = LocalDate.now(); //http://www.java2s.com/Tutorials/Java/java.time/LocalDate/index.htm
        int currentMonth = today.getMonthValue(); //Kas kuup2evade jaoks v6iks ka eraldi klassi teha?

        Label[][] months, categories, amounts;

        categories = new Label[categoryList.size()][1];
        months = new Label[1][numberOfMonths];

        amounts = new Label[categoryList.size()][numberOfMonths];


        for (int i = 0; i < categoryList.size(); i++) {
            categories[i][0] = new Label();
            categories[i][0].setText((categoryList.get(i)).toString());
            categories[i][0].setPrefSize(150, 25);
            categories[i][0].setPadding(new Insets(0, 15, 0, 15));
            categories[i][0].setId("tableHeader");
            table.add(categories[i][0], 0, i + 1);
        }

        for (int j = 0; j < numberOfMonths; j++) {
            months[0][j] = new Label();
            months[0][j].setText((today.getMonth().minus(numberOfMonths-j-1)).getDisplayName(TextStyle.SHORT, Locale.ROOT));
            months[0][j].setFont(Font.font("Arial", FontWeight.BOLD, 20));
            months[0][j].setPrefSize(100,25);
            months[0][j].setPadding(new Insets(0,15,0,15));
            months[0][j].setId("tableHeader");
            table.add(months[0][j],j+1,0);
        }

        for (int i = 0; i < categoryList.size(); i++) {
            for (int j = 0; j < numberOfMonths; j++) {

                amounts[i][j] = new Label();
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
                amounts[i][j].setText(db.getPeriodAmountByCategories(category, startDate, endDate).toString());
                amounts[i][j].setPadding(new Insets(0,15,0,15));
                amounts[i][j].setId("tableData");
                table.add(amounts[i][j], j + 1, i + 1);
                table.setAlignment(Pos.CENTER); //Et tabel oleks ProgramScreenil keskel, tuleb see ka siin ‰ra m‰‰rata
                table.setPadding(new Insets(10,0,10,0));

            }
        }
        db.closeConnection();
        return table;
    }

    public void kuup2evaTestimine() {
        //http://www.java2s.com/Tutorials/Java/java.time/LocalDate/index.htm

        int numberOfMonths = 12;

        for (int i = 1; i < 13; i++) {
            for (int j = 0; j < numberOfMonths; j++) {
                LocalDate today = LocalDate.now().withMonth(i);
                LocalDate startDate,endDate;
                ArrayList kuud = new ArrayList();
                int currentMonth = today.getMonthValue(); //Kas kuup2evade jaoks v6iks ka eraldi klassi teha?

                if (currentMonth > (85) ) {
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
                kuud.add(startDate);
                System.out.print("Alguskuu on: " +i+ "   ");
                System.out.println(kuud);
            }
        }
    }

}
