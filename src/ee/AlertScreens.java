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
 */
public class AlertScreens {  //Erinevad teavitusaknad
    Stage alertScreen = new Stage();
    int sceneHeight = 100;
    int sceneWidth = 400;



    public void userRegistered() { //Teavitus, kui kasutaja on edukalt registreeritud


        StackPane sp = new StackPane();
        Scene sc = new Scene(sp,sceneWidth,sceneHeight);
        sc.getStylesheets().add(getClass().getResource("css/test.css").toExternalForm());
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
        alertScreen.setOnCloseRequest(event -> alertScreen.close());
        sp.getChildren().addAll(message,buttonOK);
        alertScreen.setScene(sc);
        alertScreen.show();
    }

    public void userAlreadyExists() { //Teavitus kui selline kasutaja on juba olemas
        StackPane sp = new StackPane();
        Scene sc = new Scene(sp,sceneWidth,sceneHeight);
        sc.getStylesheets().add(getClass().getResource("css/test.css").toExternalForm());
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph

        Text message = new Text("Username already exists. Pick another username or log in");
        sp.setAlignment(message, Pos.TOP_CENTER);
        Button buttonOK = new Button("OK");
        buttonOK.setOnAction(event -> {
            alertScreen.close();
        });

        alertScreen.setTitle("Notification");
        alertScreen.setOnCloseRequest(event -> alertScreen.close());
        sp.getChildren().addAll(message,buttonOK);
        alertScreen.setScene(sc);
        alertScreen.show();
    }

    public void passwordIncorrect() { //Teavitus vale parooli sisestamisest
        StackPane sp = new StackPane();
        Scene sc = new Scene (sp, sceneWidth,sceneHeight);
        sc.getStylesheets().add(getClass().getResource("css/test.css").toExternalForm());
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph
        Text message = new Text ("Incorrect fieldPassword. If you have forgotten your fieldPassword then try to remember it");
        sp.setAlignment(message, Pos.TOP_CENTER);
        Button buttonOK = new Button("OK");
        buttonOK.setOnAction(event -> {
            alertScreen.close();
        });

        alertScreen.setTitle ("Wrong fieldPassword");
        alertScreen.setOnCloseRequest(event -> alertScreen.close());
        sp.getChildren().addAll(message,buttonOK);
        alertScreen.setScene(sc);
        alertScreen.show();
    }
}
