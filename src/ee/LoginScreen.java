package ee;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by Maila on 14/11/2015.
 */
public class LoginScreen {
    Stage screenMain = new Stage();
    TextField userName;
    PasswordField password;
    Button buttonLogin, buttonAddUser;
    int sceneHeight = 600;
    int sceneWidth = 600;
    int buttonWidth = sceneWidth/3;


    LoginScreen() {
        setupScene();
        setupRegister();
    }

    private void setupScene() {

        VBox layoutMain = new VBox();
        Scene sceneMain = new Scene(layoutMain,sceneHeight,sceneWidth);

        screenMain.setTitle("Main window");
        //Kujundus normaalseks teha. nt lahtrite pikkused, asetused jms
        //set on close request lisada
        Text programTitle = new Text("Eriti Vinge Programm");
        programTitle.setFont(Font.font("Arial", 20));
        programTitle.setFill(Color.MEDIUMVIOLETRED);

        Label unLabel = new Label("Username");
        userName = new TextField();
        Label pwLabel = new Label ("Password");
        password = new PasswordField();
        buttonLogin = new Button ("Log in");
        buttonLogin.setPrefWidth(buttonWidth);
        buttonAddUser = new Button ("Sign up");
        buttonAddUser.setPrefWidth(buttonWidth);

        layoutMain.getChildren().addAll(programTitle,unLabel,userName,pwLabel,password, buttonLogin, buttonAddUser);

        screenMain.setScene(sceneMain);
        screenMain.show();

    }

    private void setupRegister() {
        buttonAddUser.setOnAction(event -> {
            screenMain.close();
            new RegisterScreen();
        });

    }

}
