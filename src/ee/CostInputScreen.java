package ee;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by Maila on 19/12/2015.
 */
public class CostInputScreen {

    Stage costInputScreen;
    Scene sc;
    int sceneHeight = 1000;
    int sceneWidth = 600;
    int width = sceneWidth/6;
    Label[][] labels;

    public CostInputScreen() {
        setupScene();
        purchaseHeader();

    }

    private void setupScene() {
        costInputScreen = new Stage();
        costInputScreen.setTitle("Cost Input");

        costInputScreen.setOnCloseRequest(event -> {
            costInputScreen.close();
            new ProgramScreen();
        });

    }

    private void purchaseHeader() {
        HBox headerLabels = new HBox();
        labels = new Label[1][3];
        for (int i = 0; i < 3; i++) {
            labels[0][i] = new Label();
            labels[0][i].setPrefWidth(width);
            headerLabels.getChildren().add(labels[0][i]);
        }
        labels[0][0].setText("Buyer");
        labels[0][1].setText("Store");
        labels[0][2].setText("Date");

        sc = new Scene(headerLabels, sceneWidth, sceneHeight);
        costInputScreen.setScene(sc);
        costInputScreen.show();
    }
}
