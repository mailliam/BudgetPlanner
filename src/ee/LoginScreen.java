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


/**
 * Created by Maila on 14/11/2015.
 */
public class LoginScreen {
    Stage screenMain = new Stage();
    TextField fieldUsername;
    PasswordField fieldPassword;
    Button buttonLogin, buttonAddUser;
    Button test, test2, katseNupp;
    int sceneHeight = 600;
    int sceneWidth = 600;
    int buttonWidth = sceneWidth/3;
    Text alertMessage;

    //katsetan esialgu selle Kristeri sql n�ite loogikaga, sest minu algne versioon meetodite viimisest Main alla tundub kohmakas ja kahtlane.
    public LoginScreen() {
        setupScene();
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
        fieldUsername = new TextField();
        Label pwLabel = new Label ("Password");
        fieldPassword = new PasswordField();

        alertMessage = new Text();


        buttonLogin = new Button ("Log in");
        buttonLogin.setPrefWidth(buttonWidth);
        buttonLogin.setOnAction(event1 -> {
            toProgram();
        });

        Text ifUserNotExist = new Text("If you don't have an account, sign up");

        buttonAddUser = new Button ("Sign up"); //nupp uue kasutaja registreerimiseks, avab registreerimisana
        buttonAddUser.setPrefWidth(buttonWidth);
        buttonAddUser.setOnAction(event -> {
            screenMain.close();
            new RegisterScreen();
        });

        katseNupp = new Button("Katsetus");
        katseNupp.setOnAction(event -> {
            new Katsetus();
        });

        test = new Button("Test: db user output");
        test.setOnAction(event -> {
            Databases dbUsers = new Databases();
            dbUsers.checkUser();
            dbUsers.closeConnection();
        });

        test2 = new Button("Test: db purchase output");
        test2.setOnAction(event -> {
            Databases dbPurchase = new Databases();
            dbPurchase.checkPurchase();
            dbPurchase.closeConnection();
        });

        layoutMain.getChildren().addAll(programTitle,unLabel, fieldUsername,pwLabel, fieldPassword, buttonLogin,alertMessage, ifUserNotExist,buttonAddUser,test,test2,katseNupp);

        screenMain.setScene(sceneMain);
        screenMain.show();
    }



    //Kui parool on �ige, siis viska programmi aken lahti. Lisaks paroolile on vaja kontrolli, kui sisestatakse vale kasutajanimi, hetkel viskab errorisse
    private void toProgram() {
        String s1 = fieldUsername.getText();
        String s2 = fieldPassword.getText();
        Databases dbUsers = new Databases();
        boolean userExists = dbUsers.checkUserExistance(s1);
        boolean passwordCorrect = dbUsers.checkPassword(s1,s2);
        if(!userExists) {
            alertMessage.setText("User does not exist");
            alertMessage.setFont(Font.font("Calibri",20)); //Kuidas fonti ja värvi seadistada: https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
            alertMessage.setFill(Color.RED);
        } else {
            if(passwordCorrect) {
                screenMain.close();
                new ProgramScreen();
            } else {
                alertMessage.setText("Incorrect Password");
                alertMessage.setFont(Font.font("Calibri",20)); //Kuidas fonti ja värvi seadistada: https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
                alertMessage.setFill(Color.RED);
            }
        }
        dbUsers.closeConnection(); //Kas siin on ikka 6ige koht yhendust sulgeda? Kas peaks j2tma lahti kuniks on v2lja logitud?
    }

}
