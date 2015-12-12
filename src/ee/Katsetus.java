package ee;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jdk.nashorn.internal.ir.Labels;

/**
 * Created by mkeerus on 12.12.15.
 */
public class Katsetus {
    Stage katsetus = new Stage();
    TextField[][] tulbad;
    Label[][] pealkirjad;
    GridPane grid;
    int rowCounter;

    public Katsetus() {
        setupScene();
    }

    private void setupScene() {
        katsetus.setTitle("Siin saab katsetada");
        katsetus.setOnCloseRequest(event -> katsetus.close());
        grid = new GridPane();
        rowCounter = 1;

        pealkirjad = new Label[1][3];
        tulbad = new TextField[rowCounter+1][3];

        for (int i = 0; i < rowCounter; i++) {
            for (int j = 0; j < 3; j++) {
                pealkirjad[0][j] = new Label("pealkiri");
                tulbad[i][j] = new TextField();
                grid.add(pealkirjad[0][j],j+1,1);
                grid.add(tulbad[i][j],j+1,i+2);
            }
        }

       tulbad[rowCounter-1][2].setOnAction(event -> {
            insertNewRow();
        });


        Scene sc = new Scene(grid,1000,1000);
        katsetus.setScene(sc);
        katsetus.show();
    }

    private void insertNewRow() {
        rowCounter = rowCounter+1;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                tulbad[i][j] = new TextField();
                grid.add(tulbad[i][j], j + 1, rowCounter + 1);
                tulbad[i][2].setOnAction(event -> {
                    insertNewRow();
                });

            }
        }
    }
}


