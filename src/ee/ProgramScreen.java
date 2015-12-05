package ee;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Maila on 25/11/2015.
 */
public class ProgramScreen {
    //Siia tuleb see aken, kust saab valida, kas tahad kulusid sisestada või päringuid teha
    Stage programScreen = new Stage();
    int sceneHeight = 100;
    int sceneWidth = 400;
    Button input, query, logout, deleteUser;

    public ProgramScreen() {
        createScene();
    }

    private void createScene() {
        programScreen.setTitle("Saidki edukalt sisse logitud!");
        programScreen.setOnCloseRequest(event -> programScreen.close());
        BorderPane bp = new BorderPane();
        Scene sc = new Scene(bp,sceneHeight,sceneWidth);
        Text text = new Text ("Siit saabki hakata sisestama");
        bp.setTop(text);
        input = new Button("Data input");
        query = new Button("Queries");
        logout = new Button("Log out");
        deleteUser = new Button("Delete user account");
        bp.setLeft(input);
        bp.setCenter(query);
        bp.setRight(logout);
        bp.setBottom(deleteUser);
        programScreen.setScene(sc);
        programScreen.show();

    }

}


