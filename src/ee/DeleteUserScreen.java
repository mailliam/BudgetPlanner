package ee;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Maila on 05/12/2015.
 */
public class DeleteUserScreen {

    public DeleteUserScreen() {
     setupScene();
     delete();
    }


    Label labelUN, labelPW;
    TextField fieldUsername;
    PasswordField fieldPassword;
    Button delete;
    Stage deleteUserScreen;


    private void setupScene() { //Kasutaja kustutamise akna seadistus
        deleteUserScreen = new Stage();
        deleteUserScreen.setOnCloseRequest(event -> deleteUserScreen.close());
        VBox v = new VBox();
        labelUN = new Label("Username");
        fieldUsername = new TextField();
        fieldUsername.setPromptText("ff"); //V6iks olla n�idatud, kes on sisse logitud, et see automaatselt
        labelPW = new Label("Password");
        fieldPassword = new PasswordField();
        delete = new Button ("Delete user");
        v.getChildren().addAll(labelUN,fieldUsername,labelPW,fieldPassword,delete);
        Scene sc = new Scene(v);
        sc.getStylesheets().add(getClass().getResource("css/test.css").toExternalForm());
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph

        deleteUserScreen.setScene(sc);
        deleteUserScreen.show();
    }

    private void delete() { //kustutamise nupu seadistus. Kontrollib l�bi andmebaasi, kas parool on �ige, ehk et kas kasutajal on �igus seda teha
        delete.setOnAction(event -> {
            String s1 = fieldUsername.getText();
            String s2 = fieldPassword.getText();
            Databases dbUsers = new Databases();
            boolean passwordCorrect = dbUsers.checkPassword(s1,s2);
            if (passwordCorrect) {
                dbUsers.deleteUser(s1);
                deleteUserScreen.close();
                new LoginScreen();
            } else {
                AlertScreens as = new AlertScreens();
                as.passwordIncorrect();
            }
            dbUsers.closeConnection();
        });
    }
}
