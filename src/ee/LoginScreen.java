package ee;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;


/**
 * Created by Maila on 14/11/2015.
 *
 * Esimene vaade - sisselogimine voi kasutajaks registreerimine
 *
 */

public class LoginScreen {
    Stage screenMain = new Stage();
    BorderPane bp;
    GridPane layoutMain;
    TextField fieldUsername;
    PasswordField fieldPassword;
    Button buttonLogin, buttonAddUser;
    Button test, test2, katseNupp;
    int sceneHeight = 600;
    int sceneWidth = 800;
    int buttonWidth = sceneWidth/3;
    Text alertMessage;


    //Kasutatud sama loogikat, mis I200 sql naites
    public LoginScreen() {
        setupScene();
        loginAndOtherFields(); //halb nimi
    }

    //Sisselogimisakna seadistus koos layoutidega
    private void setupScene() {

        bp = new BorderPane();
        layoutMain = new GridPane();
        layoutMain.setAlignment(Pos.TOP_CENTER);
        layoutMain.setVgap(5);
        layoutMain.setHgap(10);

        ColumnConstraints column1 = new ColumnConstraints(); //http://docs.oracle.com/javafx/2/layout/size_align.htm
        column1.setHalignment(HPos.RIGHT);
        column1.setPrefWidth(200);
        layoutMain.getColumnConstraints().add(column1);

        ColumnConstraints column2 = new ColumnConstraints(); //http://docs.oracle.com/javafx/2/layout/size_align.htm
        column2.setPrefWidth(200);
        layoutMain.getColumnConstraints().add(column2);

        Scene sceneMain = new Scene(bp,sceneWidth,sceneHeight);
        sceneMain.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        //CSS loomisel kasutasin:
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph
        //http://hg.openjdk.java.net/openjfx/8/master/rt/file/f89b7dc932af/modules/controls/src/main/resources/com/sun/javafx/scene/control/skin/modena/modena.css
        //https://docs.oracle.com/javafx/2/charts/css-styles.htm
        //Kuidas fonti ja värvi seadistada: https://docs.oracle.com/javafx/2/text/jfxpub-text.htm

        screenMain.setTitle("Main window");
        screenMain.setOnCloseRequest(event -> screenMain.close());
        screenMain.setScene(sceneMain);
        screenMain.show();
    }

    //Node'de tekitamine ning lisamine vastavatesse layoutidesse
    private void loginAndOtherFields () {
        Text programTitle = new Text("Family's cost tracker");
        programTitle.setId("Header");
        bp.setTop(programTitle);
        bp.setAlignment(programTitle, Pos.CENTER);
        bp.setCenter(layoutMain);

        Label labelUsername = new Label("Username");
        labelUsername.setAlignment(Pos.CENTER_RIGHT);
        fieldUsername = new TextField();
        Label labelPassword = new Label ("Password");
        labelPassword.setAlignment(Pos.CENTER_RIGHT);
        fieldPassword = new PasswordField();

        alertMessage = new Text();
        alertMessage.setId("alert"); //Id maaramine, et formaadi saaks css-ga paika panna. Millegiparast varv ei toimi

        buttonLogin = new Button ("Log in"); //nupp sisselogimiseks
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

        katseNupp = new Button("katse");
        katseNupp.setOnAction(event -> {
            Databases db = new Databases();
            Tables tbl = new Tables();
            Graphs gr = new Graphs();
            //db.checkPurchase();
            //System.out.println(db.getBuyerList());
            //tbl.amountLastMonthsByCategories();
            tbl.kuup2evaTestimine();
            //gr.amountLastMonthsByCategories();
        });

        //Lisab k6ik node'd layoutMaini, mis on GridPane
        layoutMain.add(labelUsername,0, 1);
        layoutMain.add(fieldUsername,1,1);
        layoutMain.add(labelPassword,0,2);
        layoutMain.add(fieldPassword,1,2);
        layoutMain.add(buttonLogin,1,3);
        layoutMain.add(alertMessage,1,4);
        layoutMain.add(ifUserNotExist,0,10);
        layoutMain.add(buttonAddUser,1,10);
        layoutMain.add(katseNupp,1,11); //2ra kustutada hiljem
    }

    //Programmi paasemine: kontroll, kas vajalikud valjad on taidetud, kas kasutaja on olemas, kas parool on 6ige
    private void toProgram() {
        String s1 = fieldUsername.getText();
        String s2 = fieldPassword.getText();
        Databases dbUsers = new Databases();
        boolean userExists = dbUsers.checkUserExistance(s1);
        boolean passwordCorrect = dbUsers.checkPassword(s1, s2);
        if (s1.isEmpty() || s2.isEmpty()) {                 //Kui valjad tuhjad: veateade
            alertMessage.setText("Username/password can not be empty");
            alertMessage.setId("alert");
        } else {
            if (!userExists) {                              //Kui kasutajat pole: veateade
                alertMessage.setText("User does not exist");
            } else {                                        //Kui parool oige: ava programm
                if (passwordCorrect) {
                    screenMain.close();
                    new ProgramScreen();
                } else {                                    //Kui parool vale: veateade
                    alertMessage.setText("Incorrect Password");
                }

                dbUsers.closeConnection();
            }
        }
    }
}
