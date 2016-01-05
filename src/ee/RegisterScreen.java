package ee;

import javafx.geometry.Insets;
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
 *
 * Registreerimisandmete t2itmine, sellise kasutaja mitteolemasolul tema registreerimine
 *
 */
public class RegisterScreen {

    Stage registerScreen = new Stage();
    TilePane layout;
    TextField fieldUsername, fieldFirstname, fieldLastname;
    PasswordField fieldPassword;
    int sceneHeight = 600;
    int sceneWidth = 500;
    Button buttonRegUser;
    Text alertMessage;

    public RegisterScreen() {
        setupScene();
        userRegistrationFields();
    }

    //Registreerimisakna seadistus koos layoutidega
    private void setupScene() {
        layout = new TilePane();
        layout.setPadding(new Insets(15,10,0,0));
        layout.setHgap(10);
        layout.setVgap(5);
        Scene scene = new Scene(layout, sceneWidth, sceneHeight);
        scene.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        //CSS lisamine:
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph

        registerScreen.setTitle("User registration");
        registerScreen.setScene(scene);
        registerScreen.show();
        registerScreen.setOnCloseRequest(event -> {  //Registreerimisakna sulgemisel vii tagasi sisselogimisakna juurde
            registerScreen.close();
            new LoginScreen();
        });
    }

    //Node'de tekitamine ning lisamine vastavatesse layoutidesse
    private void userRegistrationFields(){
        Label labelUsername = new Label("Username");
        fieldUsername = new TextField();
        Label labelPassword = new Label("Password");
        fieldPassword = new PasswordField();
        Label labelFirstname = new Label("First name");
        fieldFirstname = new TextField();
        Label labelLastname = new Label("Last name");
        fieldLastname = new TextField();

        alertMessage = new Text();

        buttonRegUser = new Button("Register user");
        buttonRegUser.setOnAction(event -> {
            registerUser();
        });

        layout.getChildren().addAll(labelUsername, fieldUsername,labelPassword, fieldPassword,labelFirstname,
                                    fieldFirstname, labelLastname, fieldLastname, alertMessage, buttonRegUser);
    }

    //Meetod kontrollib labi andmebaasi, kas kasutaja on olemas. Kui ei, siis registreerib, kui jah, teavitab, et valitagu uus kasutajanimi v6i logigu sisse
    private void registerUser() {
        String s1 = fieldUsername.getText();
        String s2 = fieldPassword.getText();
        String s3 = fieldFirstname.getText();
        String s4 = fieldLastname.getText();
        Databases dbUsers = new Databases();
        boolean userExists = dbUsers.checkUserExistance(s1);
        System.out.println(userExists);
        if (s1.isEmpty()||s2.isEmpty()||s3.isEmpty()||s4.isEmpty()){ //Kui moni vali on tuhi: veateade
            alertMessage.setText("No fields can be left empty!");
            alertMessage.setFill(Color.RED);
        } else {
            if (!userExists){                                       //Kui kasutajat pole: registreeri
                dbUsers.registerUser(s1,s2,s3,s4);
                dbUsers.closeConnection();
                AlertScreens as = new AlertScreens();
                as.userRegistered();
                registerScreen.close();
            } else {                                                //Kui kasutaja on: veateade
                dbUsers.closeConnection();
                alertMessage.setText("User already exists.");
                fieldUsername.clear();
                fieldPassword.clear();
                fieldFirstname.clear();
                fieldLastname.clear();
                alertMessage.setFill(Color.RED);
            }
        }
    }
}
