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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import java.time.format.TextStyle;
import java.time.temporal.ChronoField;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by Maila on 31/12/2015.
 *
 * Klass, mis loob erinevate p‰ringute jaoks tabeleid
 *
 */
public class Tables {
    int categoryWidth = 200;
    int dataWidth = 100;
    int height = 25;

    public Tables() {

    }

    //Arvutab etteantud arvu eelmiste kuude ostud kasutades Databases objekti meetodeid ja paneb need tabelisse
    public GridPane amountLastMonthsByCategories(int numberOfMonths) {
        Databases db = new Databases();
        ArrayList categoryList = db.getCategoryList();
        GridPane table = new GridPane();
        table.setGridLinesVisible(true);

        LocalDate today = LocalDate.now(); //http://www.java2s.com/Tutorials/Java/java.time/LocalDate/index.htm
        int currentMonth = today.getMonthValue();

        Label[][] months, categories, amounts, totalAmount;

        categories = new Label[categoryList.size()][1];
        months = new Label[1][numberOfMonths];
        amounts = new Label[categoryList.size()][numberOfMonths];
        totalAmount = new Label[1][numberOfMonths];

        //Kategooriad tabelisse
        for (int i = 0; i < categoryList.size(); i++) {
            categories[i][0] = new Label();
            categories[i][0].setText((categoryList.get(i)).toString());
            categories[i][0].setPrefSize(categoryWidth, height);
            categories[i][0].setPadding(new Insets(0, 15, 0, 15));
            categories[i][0].setId("tableHeader");
            table.add(categories[i][0], 0, i + 1);
        }

        //Kuud tabelisse
        for (int j = 0; j < numberOfMonths; j++) {
            months[0][j] = new Label();
            months[0][j].setText((today.getMonth().minus(numberOfMonths-j-1)).getDisplayName(TextStyle.SHORT, Locale.ROOT));
            months[0][j].setPrefSize(dataWidth,height);
            months[0][j].setPadding(new Insets(0,15,0,15));
            months[0][j].setId("tableHeader");
            table.add(months[0][j],j+1,0);
        }

        //Vastavate kuude ja kategooriate summad tabelisse
        for (int i = 0; i < categoryList.size(); i++) {
            for (int j = 0; j < numberOfMonths; j++) {

                amounts[i][j] = new Label();
                LocalDate startDate,endDate;

                //Arvutus eelnevate kuude algus- ja lıppkuup‰evade kohta
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

                //Kogusumma arvutus ja tabelisse panek
                Label labelTotal = new Label("Total");
                labelTotal.setId("tableHeader");
                labelTotal.setPrefSize(categoryWidth, height);
                labelTotal.setPadding(new Insets(0,15,0,15));
                totalAmount[0][j] = new Label();
                totalAmount[0][j].setId("tableHeader");
                totalAmount[0][j].setPadding(new Insets(0,15,0,15));
                totalAmount[0][j].setText(db.getPeriodAmount(startDate, endDate).toString());
                table.add(labelTotal,0,categoryList.size()+1);
                table.add(totalAmount[0][j], j+1, categoryList.size()+1);
            }
        }

        db.closeConnection();
        return table;
    }

    //Perioodi summa kategooriate kaupa
    public GridPane periodAmountByCategories(LocalDate startDate, LocalDate endDate) {
        Databases db = new Databases();
        ArrayList categoryList = db.getCategoryList();
        GridPane table = new GridPane();
        table.setGridLinesVisible(true);

        Label[][] tableFields = new Label[categoryList.size()][2];
        Label totalAmount = new Label();

        for (int i = 0; i < categoryList.size(); i++) {
            for (int j = 0; j < 2; j++) {
                String category = categoryList.get(i).toString();
                BigDecimal amount = db.getPeriodAmountByCategories(category, startDate, endDate);
                BigDecimal c = new BigDecimal("0");
                tableFields[i][j] = new Label();
                tableFields[i][j].setPadding(new Insets(0, 15, 0, 15));

                //Sisestab tabelisse ainult need read, kus vastaval perioodil on oste toimunud
                if(!amount.equals(c)) {
                    switch (j) {
                        case 0:
                            tableFields[i][0].setText(category);
                            tableFields[i][0].setPrefSize(categoryWidth,height);
                            tableFields[i][0].setId("tableHeader");
                            table.add(tableFields[i][0],0,i);
                            break;
                        case 1:
                            tableFields[i][1].setText(amount.toString());
                            tableFields[i][1].setId("tableData");
                            table.add(tableFields[i][1],1,i);
                            break;
                    }
                }
            }
        }

        table.setAlignment(Pos.CENTER); //Et tabel oleks ProgramScreenil keskel, tuleb see ka siin ‰ra m‰‰rata
        table.setPadding(new Insets(10,0,10,0));

        //Kogusumma arvutus ja tabelisse lisamine
        Label labelTotal = new Label("Total");
        labelTotal.setId("tableHeader");
        labelTotal.setPrefSize(categoryWidth, height);
        labelTotal.setPadding(new Insets(0,15,0,15));

        totalAmount.setId("tableHeader");
        totalAmount.setPadding(new Insets(0, 15, 0, 15));
        totalAmount.setText(db.getPeriodAmount(startDate, endDate).toString());
        table.add(labelTotal, 0, categoryList.size()+1);
        table.add(totalAmount, 1, categoryList.size()+1);

        db.closeConnection();
        return table;
    }

    //Perioodi summad kategoorias olevate esemete kaupa
    public GridPane periodAmountByItemsInCategory (String category, LocalDate startDate, LocalDate endDate) {
        Databases db = new Databases();
        ArrayList itemList = db.getItemListForCategory(category);
        GridPane table = new GridPane();
        table.setGridLinesVisible(true);

        Label[][] tableFields = new Label[itemList.size()][2];
        Label totalAmount = new Label();

        for (int i = 0; i < itemList.size(); i++) {
            for (int j = 0; j < 2; j++) {
                String item = itemList.get(i).toString();
                BigDecimal amount = db.getPeriodAmountByItems(item, startDate, endDate);
                BigDecimal c = new BigDecimal("0");
                tableFields[i][j] = new Label();
                tableFields[i][j].setPadding(new Insets(0, 15, 0, 15));

                //Lisab tabelisse ainult need read, kus on antud perioodil oste olnud
                if(!amount.equals(c)) {
                    switch (j) {
                        case 0:
                            tableFields[i][0].setText(item);
                            tableFields[i][0].setPrefSize(categoryWidth,height);
                            tableFields[i][0].setId("tableHeader");
                            table.add(tableFields[i][0],0,i);
                            break;
                        case 1:
                            tableFields[i][1].setText(amount.toString());
                            tableFields[i][1].setId("tableData");
                            table.add(tableFields[i][1],1,i);
                            break;
                    }
                }
            }
        }

        table.setAlignment(Pos.CENTER); //Et tabel oleks ProgramScreenil keskel, tuleb see ka siin ‰ra m‰‰rata
        table.setPadding(new Insets(10,0,10,0));

        //Kogusumma arvutamine ja tabelisse panek
        Label labelTotal = new Label("Total");
        labelTotal.setId("tableHeader");
        labelTotal.setPrefSize(250, 25);
        labelTotal.setPadding(new Insets(0,15,0,15));

        totalAmount.setId("tableHeader");
        totalAmount.setPadding(new Insets(0, 15, 0, 15));
        totalAmount.setText(db.getPeriodAmountByCategories(category, startDate, endDate).toString());
        table.add(labelTotal, 0, itemList.size()+1);
        table.add(totalAmount, 1, itemList.size()+1);

        db.closeConnection();
        return table;
    }

}
