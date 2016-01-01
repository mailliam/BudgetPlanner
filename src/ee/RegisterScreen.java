package ee;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;



/**
 * Created by Maila on 24/11/2015.
 */
public class RegisterScreen {

    Stage registerScreen = new Stage();
    TextField fieldUsername, fieldFirstname, fieldLastname;
    PasswordField fieldPassword;
    int sceneHeight = 600;
    int sceneWidth = 500;
    Button buttonRegUser;
    Text alertMessage;

    int buttonWidth = sceneWidth/3;

    public RegisterScreen() {
        setupScene();
    }

    private void setupScene() { //Registreerimisakna seadistus
        TilePane layout = new TilePane();
        layout.setHgap(10);
        layout.setVgap(5);
        Scene scene = new Scene(layout, sceneWidth, sceneHeight);
        scene.getStylesheets().add(getClass().getResource("css/test.css").toExternalForm());
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph


        registerScreen.setTitle("User registration");
        Label un = new Label("Username");
        fieldUsername = new TextField();
        Label pw = new Label("Password");
        fieldPassword = new PasswordField();
        Label fn = new Label("First name");
        fieldFirstname = new TextField();
        Label ln = new Label("Last name");
        fieldLastname = new TextField();

        alertMessage = new Text();

        buttonRegUser = new Button("Register user");
        buttonRegUser.setOnAction(event -> {
            registerUser();
        });

        layout.getChildren().addAll(un, fieldUsername,pw, fieldPassword,fn, fieldFirstname, ln, fieldLastname, alertMessage, buttonRegUser);
        registerScreen.setScene(scene);
        registerScreen.show();
        registerScreen.setOnCloseRequest(event -> {
            registerScreen.close();
            new LoginScreen();
        });
    }


    private void registerUser() { //Registreerimisnupu seadistus: kontrollib l�bi andmebaasi, kas kasutaja on olemas. kui ei, siis registreerib, kui jah, teavitab, et valitagu uus kasutajanimi
        String s1 = fieldUsername.getText();
        String s2 = fieldPassword.getText();
        String s3 = fieldFirstname.getText();
        String s4 = fieldLastname.getText();
        Databases dbUsers = new Databases();
        boolean userExists = dbUsers.checkUserExistance(s1);
        System.out.println(userExists);
        if (s1.isEmpty()||s2.isEmpty()||s3.isEmpty()||s4.isEmpty()){
            alertMessage.setText("No fields can be left empty!");
            alertMessage.setFill(Color.RED);
            fieldUsername.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;"); //TextFieldi muutmine: http://stackoverflow.com/questions/24231610/javafx-red-border-for-text-field
        } else {
            if (!userExists){
                dbUsers.registerUser(s1,s2,s3,s4);
                dbUsers.closeConnection();
                registerScreen.close();
            } else {
                AlertScreens as = new AlertScreens();
                as.userAlreadyExists();
                dbUsers.closeConnection();
            }
        }
    }
}
