package ee;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Maila on 28/11/2015.
 *
 * Erinevad teavitusaknad
 *
 */
public class AlertScreens {
    Stage alertScreen = new Stage();
    int sceneHeight = 100;
    int sceneWidth = 300;

    //Teavitus, kui kasutaja on edukalt registreeritud
    public void userRegistered() {
        StackPane sp = new StackPane();
        Scene sc = new Scene(sp,sceneWidth,sceneHeight);
        sc.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph

        Text message = new Text("User succesfully registered");
        sp.setAlignment(message, Pos.TOP_CENTER);
        Button buttonOK = new Button("OK");
        buttonOK.setOnAction(event -> {
            alertScreen.close();
            new LoginScreen();
        });

        alertScreen.setTitle("Notification");
        alertScreen.setOnCloseRequest(event -> {
            alertScreen.close();
            new LoginScreen();
        });
        sp.getChildren().addAll(message,buttonOK);
        alertScreen.setScene(sc);
        alertScreen.show();
    }

    //Teavitus vale parooli sisestamisest
    public void passwordIncorrect() {
        StackPane sp = new StackPane();
        Scene sc = new Scene (sp, sceneWidth,sceneHeight);
        sc.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph
        Text message = new Text ("Incorrect Password");
        sp.setAlignment(message, Pos.TOP_CENTER);
        Button buttonOK = new Button("OK");
        buttonOK.setOnAction(event -> {
            alertScreen.close();
        });

        alertScreen.setTitle ("Incorrect Password");
        alertScreen.setOnCloseRequest(event -> alertScreen.close());
        sp.getChildren().addAll(message,buttonOK);
        alertScreen.setScene(sc);
        alertScreen.show();
    }
}
