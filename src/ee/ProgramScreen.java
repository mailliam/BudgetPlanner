package ee;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Maila on 25/11/2015.
 */
public class ProgramScreen {
    //Siia tuleb see aken, kust saab valida, kas tahad kulusid sisestada v�i p�ringuid teha
    Stage programScreen = new Stage();
    BorderPane bp;
    int sceneHeight = 700;
    int sceneWidth = 700;
    int buttonWidth_1 = 100;
    Button input, query, logout, deleteUser;

    public ProgramScreen() {
        setupScene();
        programButtons();
        showGraph();
    }

    private void setupScene() {
        programScreen.setTitle("Program");
        programScreen.setOnCloseRequest(event -> programScreen.close());
        bp = new BorderPane();
        Scene sc = new Scene(bp,sceneWidth,sceneHeight);
        programScreen.setScene(sc);
        programScreen.show();
    }

    private void programButtons() {

        VBox v = new VBox();

        input = new Button("Data input");
        input.setPrefWidth(buttonWidth_1);
        input.setOnAction(event -> toCostInputScreen());

        query = new Button("Queries");
        query.setPrefWidth(buttonWidth_1);
        query.setOnAction(event -> toQueryScreen());

        v.getChildren().addAll(input,query);
        bp.setLeft(v);

        HBox h = new HBox();

        logout = new Button("Log out");
        logout.setOnAction(event -> logoutUser());

        deleteUser = new Button("Delete user account");
        deleteUser.setOnAction(event -> deleteAccount());

        h.getChildren().addAll(logout,deleteUser);

        bp.setBottom(h);
    }

    private void showGraph() {
        Graphs graphs = new Graphs();
        graphs.amountByBuyers();
        bp.setCenter(graphs.amountByBuyers());
    }


    private void toCostInputScreen() {
        programScreen.close();
        new CostInputScreen();
    }

    private void toQueryScreen() {
        programScreen.close();
        new QueryScreen();
    }

    private void logoutUser() {
        programScreen.close();
        new LoginScreen();
    }


    private void deleteAccount() {
        programScreen.close();
        new DeleteUserScreen();
    }

}


