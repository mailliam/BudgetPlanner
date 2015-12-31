package ee;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.Month;
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
        db.closeConnection();
        GridPane table = new GridPane();
        table.setGridLinesVisible(true);
        
        table.setHgap(5);
        table.setVgap(5);
        LocalDate today = LocalDate.now(); //http://www.java2s.com/Tutorials/Java/java.time/LocalDate/index.htm
        Month this_Month = today.getMonth(); //Kas kuup2evade jaoks v6iks ka eraldi klassi teha?
        Month prev_1_Month = today.getMonth().minus(1);
        Month prev_2_Month = today.getMonth().minus(2);
        Label[][] months, categories;
        System.out.println(this_Month);
        System.out.println(prev_1_Month);
        System.out.println(prev_2_Month);
        categories = new Label[categoryList.size()][1];
        months = new Label[1][3];

        for (int i = 0; i < categoryList.size(); i++) {
            categories[i][0] = new Label();
            categories[i][0].setText((categoryList.get(i)).toString());
            categories[i][0].setFont(Font.font("Arial", FontWeight.BOLD, 12));
            table.add(categories[i][0], 0, i+1);
        }

        for (int j = 0; j < 3; j++) {
            months[0][j] = new Label();
            months[0][j].setText((today.getMonth().minus(j)).toString());
            table.add(months[0][j],j+1,0);
            months[0][j].setFont(Font.font("Arial", FontWeight.BOLD, 20));
        }
        return table;
    }

}
