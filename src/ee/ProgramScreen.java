package ee;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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

    public ProgramScreen() {
        setupScene();
        programMenu();
        showMainTableAndGraph();
    }

    private void setupScene() {
        programScreen.setTitle("Program");
        programScreen.setOnCloseRequest(event -> programScreen.close());
        bp = new BorderPane();
        Scene sc = new Scene(bp,sceneWidth,sceneHeight);
        sc.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        //CSS lisamiseks sain abi:
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph


        programScreen.setScene(sc);
        programScreen.show();
    }

    private void programMenu () {
        //Menyy loomine JavaFX GUI Tutorial videode abil:
        //https://www.youtube.com/watch?v=JBJ9MIEfU3k&index=21&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
        //https://www.youtube.com/watch?v=AP4e6Lxncp4&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG&index=22

        //Menüüpunktide loomine ja tegevuste seadistamine
        Menu fileMenu = new Menu("_File");

        MenuItem newPurchase = new MenuItem("New purchase");
        fileMenu.getItems().add(newPurchase);
        newPurchase.setOnAction(event -> toCostInputScreen());

        MenuItem query = new MenuItem("Query");
        fileMenu.getItems().add(query);
        query.setOnAction(event -> toQueryScreen());

        MenuItem logout = new MenuItem("Log out");
        fileMenu.getItems().add(logout);
        logout.setOnAction(event -> logoutUser());

        Menu settingsMenu = new Menu("_Settings");
        MenuItem categories = new MenuItem("Categories");
        settingsMenu.getItems().add(categories);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, settingsMenu);

        bp.setTop(menuBar);
    }

    private void showMainTableAndGraph() {
        VBox v = new VBox();
        v.setPadding(new Insets(20,20,20,20));
        Graphs graph = new Graphs();
        graph.amountByBuyers();
        Tables table = new Tables();
        v.getChildren().addAll(table.amountLastMonthsByCategories(), graph.amountByBuyers());
        bp.setCenter(v);
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


