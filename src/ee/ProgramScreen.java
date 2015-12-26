package ee;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Maila on 25/11/2015.
 */
public class ProgramScreen {
    //Siia tuleb see aken, kust saab valida, kas tahad kulusid sisestada v�i p�ringuid teha
    Stage programScreen = new Stage();
    int sceneHeight = 200;
    int sceneWidth = 400;
    int buttonWidth_1 = 100;
    Button input, query, logout, deleteUser;
    public ProgramScreen() {
        setupScene();
        toCostInputScreen();
        toQueryScreen();
        deleteAccount();
        logoutUser();
    }

    private void setupScene() {
        programScreen.setTitle("Saidki edukalt sisse logitud!");
        programScreen.setOnCloseRequest(event -> programScreen.close());
        BorderPane bp = new BorderPane();
        Scene sc = new Scene(bp,sceneWidth,sceneHeight);
        Text text = new Text ("Siit saabki hakata sisestama");
        bp.setTop(text);
        VBox v = new VBox();
        input = new Button("Data input");
        input.setPrefWidth(buttonWidth_1);
        query = new Button("Queries");
        query.setPrefWidth(buttonWidth_1);
        v.getChildren().addAll(input,query);
        HBox h = new HBox();
        logout = new Button("Log out");

        deleteUser = new Button("Delete user account");

        h.getChildren().addAll(logout,deleteUser);
        bp.setLeft(v);
        bp.setBottom(h);
        programScreen.setScene(sc);
        programScreen.show();
    }


    private void toCostInputScreen() {
        input.setOnAction(event -> {
            programScreen.close();
            new CostInputScreen();
        });
    }

    private void toQueryScreen() {
        query.setOnAction(event -> {
            programScreen.close();
            new QueryScreen();
        });
    }

    private void logoutUser() {
        logout.setOnAction(event -> {
            programScreen.close();
            new LoginScreen();
        });
    }

    private void deleteAccount() {
        deleteUser.setOnAction(event -> {
            programScreen.close();
            new DeleteUserScreen();
        });
    }

}


