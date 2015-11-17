package ee;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Created by Maila on 14/11/2015.
 */
public class UserInterface extends Application {
    Stage windowMain;
    Scene sceneMain;
    Button input, query;
    int buttonWidth = 100;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        windowMain = primaryStage;
        windowMain.setTitle("Main window");

        input = new Button("Input");
        input.setPrefWidth(buttonWidth);
        input.setOnAction(event -> InputWindow.inputWindow());

        query = new Button("Queries");
        query.setPrefWidth(buttonWidth);

        VBox layoutMain = new VBox(3);
        layoutMain.getChildren().addAll(input,query);
        sceneMain = new Scene(layoutMain,200,200);
        windowMain.setScene(sceneMain);
        windowMain.show();
    }

}
