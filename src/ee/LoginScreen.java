package ee;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;


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
    int sceneWidth = 800;
    int buttonWidth = sceneWidth/3;
    Text alertMessage;


    //katsetan esialgu selle Kristeri sql n�ite loogikaga, sest minu algne versioon meetodite viimisest Main alla tundub kohmakas ja kahtlane.
    public LoginScreen() {
        setupScene();
    }

    //Esimene aken, kustkaudu saab sisselogida ja kasutajat registreerima hakata
    private void setupScene() { //Seadistab esimese akna vajalikud atribuudid ja tegevused nende kylge. Ilmselt tuleks pisut lahti kirjutada osasid, muidu tuleb yks pikk meetod.

        GridPane layoutMain = new GridPane();
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

        Scene sceneMain = new Scene(layoutMain,sceneWidth,sceneHeight);
        sceneMain.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph

        screenMain.setTitle("Main window");
        screenMain.setOnCloseRequest(event -> screenMain.close());

        Text programTitle = new Text("Family's cost tracker");
        programTitle.setId("Header");

        Label unLabel = new Label("Username");
        unLabel.setAlignment(Pos.CENTER_RIGHT);
        fieldUsername = new TextField();
        Label pwLabel = new Label ("Password");
        pwLabel.setAlignment(Pos.CENTER_RIGHT);
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

        katseNupp = new Button("katse");
        katseNupp.setOnAction(event -> {
            Databases db = new Databases();
            Tables tbl = new Tables();
            //db.checkPurchase();
            //System.out.println(db.getBuyerList());
            //tbl.amountLastMonthsByCategories();
            tbl.kuup2evaKatsetus();


        });

        test = new Button("Test: db user output");
        test.setOnAction(event -> {
            Databases db = new Databases();
            db.checkUser();
            db.closeConnection();
        });

        test2 = new Button("Test: db purchase output");
        test2.setOnAction(event -> {
            Databases db = new Databases();
            db.checkPurchase();
            db.closeConnection();
        });

        layoutMain.add(programTitle,0,0);
        layoutMain.add(unLabel,0, 1);
        layoutMain.add(fieldUsername,1,1);
        layoutMain.add(pwLabel,0,2);
        layoutMain.add(fieldPassword,1,2);
        layoutMain.add(buttonLogin,1,3);
        layoutMain.add(alertMessage,1,4);
        layoutMain.add(ifUserNotExist,0,10);
        layoutMain.add(buttonAddUser,1,10);
        layoutMain.add(katseNupp,1,11); //2ra kustutada hiljem

        screenMain.setScene(sceneMain);
        screenMain.show();
    }



    //Kui parool on 6ige, siis viska programmi aken lahti.
    private void toProgram() {
        String s1 = fieldUsername.getText();
        String s2 = fieldPassword.getText();
        Databases dbUsers = new Databases();
        boolean userExists = dbUsers.checkUserExistance(s1);
        boolean passwordCorrect = dbUsers.checkPassword(s1, s2);
        if (s1.isEmpty() || s2.isEmpty()) {
            alertMessage.setText("Username/password can not be empty");
            alertMessage.setFont(Font.font("Calibri", 20)); //Kuidas fonti ja värvi seadistada: https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
            alertMessage.setFill(Color.RED);
        } else {
            if (!userExists) {
                alertMessage.setText("User does not exist");
                alertMessage.setFont(Font.font("Calibri", 20)); //Kuidas fonti ja värvi seadistada: https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
                alertMessage.setFill(Color.RED);
            } else {
                if (passwordCorrect) {
                    screenMain.close();
                    new ProgramScreen();
                } else {
                    alertMessage.setText("Incorrect Password");
                    alertMessage.setFont(Font.font("Calibri", 20)); //Kuidas fonti ja värvi seadistada: https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
                    alertMessage.setFill(Color.RED);
                }

                dbUsers.closeConnection();
            }
        }
    }
}
