package ee;

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

import java.util.Scanner;


/**
 * Created by Maila on 14/11/2015.
 */
public class LoginScreen {
    Stage screenMain = new Stage();
    TextField userName;
    PasswordField password;
    Button buttonLogin, buttonAddUser;
    Button test;
    int sceneHeight = 600;
    int sceneWidth = 600;
    int buttonWidth = sceneWidth/3;

//katsetan esialgu selle Kristeri sql n�ite loogikaga, sest minu algne versioon meetodite viimisest Main alla tundub kohmakas ja kahtlane. Pealegi ei saa psvm (ja ka start?) kasutada mittestatic meetodeid.

    public LoginScreen() {
        setupScene();
        toRegisterScreen();
        toInputScreen();
    }

    //Esimene aken, kustkaudu saab sisselogida ja kasutajat registreerima hakata
    private void setupScene() {

        VBox layoutMain = new VBox();
        Scene sceneMain = new Scene(layoutMain,sceneWidth,sceneHeight);

        screenMain.setTitle("Main window");
        screenMain.setOnCloseRequest(event -> screenMain.close());

        //Kujundus normaalseks teha. nt lahtrite pikkused, asetused jms
        Text programTitle = new Text("Eriti Vinge Programm");
        programTitle.setFont(Font.font("Arial", 20));
        programTitle.setFill(Color.MEDIUMVIOLETRED);

        Label unLabel = new Label("Username");
        userName = new TextField();
        Label pwLabel = new Label ("Password");
        password = new PasswordField();
        buttonLogin = new Button ("Log in");
        buttonLogin.setPrefWidth(buttonWidth);
        Text ifUserNotExist = new Text("If you don't have an account, sign up");
        buttonAddUser = new Button ("Sign up");
        buttonAddUser.setPrefWidth(buttonWidth);

        test = new Button("Test: db output");
        test.setOnAction(event -> {
            DatabaseUsers dbUsers = new DatabaseUsers();
            dbUsers.checkUser();

        });

        layoutMain.getChildren().addAll(programTitle,unLabel,userName,pwLabel,password, buttonLogin, ifUserNotExist,buttonAddUser,test);

        screenMain.setScene(sceneMain);
        screenMain.show();
    }
    //viska registreerimisaken lahti. Tegelikult ei ole neil meetoditel siin eriilst m�tet. need v�iks ju kohe buttni alla panna.
    private void toRegisterScreen() {
        buttonAddUser.setOnAction(event -> {
            screenMain.close();
            new RegisterScreen();
        });
    }

    //Kui parool on �ige, siis viska sisestuse aken lahti. tegelikult v�iks vist mingi vaheasja siia teha, aga hiljem
    private void toInputScreen() {
        buttonLogin.setOnAction(event -> {
            screenMain.close();
            new ProgramScreen();
        });

    }

}