package ee;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

/**
 * Created by Maila on 24/11/2015.
 */
public class RegisterScreen {

    Stage registerScreen = new Stage();
    TextField userName, firstName, lastName;
    PasswordField password;
    Button buttonRegister, buttonExit;
    int sceneHeight = 600;
    int sceneWidth = 500;
    int buttonWidth = sceneWidth/3;

    RegisterScreen() {
        setupScene();
    }

    private void setupScene() {
        TilePane layout = new TilePane();
        layout.setPrefSize(sceneWidth/2,buttonWidth/8);
        layout.setHgap(10);
        layout.setHgap(5);
        Scene scene = new Scene(layout, sceneWidth, sceneHeight);

        registerScreen.setTitle("User registration");
        Label un = new Label("Username");
        userName = new TextField();
        Label pw = new Label("Password");
        password = new PasswordField();
        Label fn = new Label("First name");
        firstName = new TextField();
        Label ln = new Label("Last name");
        lastName = new TextField();

        layout.getChildren().addAll(un,userName,pw,password,fn, firstName, ln, lastName);
        registerScreen.setScene(scene);
        registerScreen.show();



    }


}
