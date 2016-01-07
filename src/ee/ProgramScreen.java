package ee;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Maila on 25/11/2015.
 *
 * Pohiline programmiaken, kust kasutaja naeb oma viimaste kuude kulusid, saab algatada uut ostu, teha paringuid
 *
 */
public class ProgramScreen {

    Stage programScreen = new Stage();
    BorderPane bp;
    int sceneHeight = 1000;
    int sceneWidth = 1000;
    RadioMenuItem view3, view6, view12;
    Text heading;
    int months;
    ToggleGroup viewPeriod;


    public ProgramScreen() {
        setupScene();
        programMenu();
        showMainTableAndGraph();
    }

    //Programmiakna seadistus koos layoutiga
    private void setupScene() {
        programScreen.setTitle("Program");
        programScreen.setOnCloseRequest(event -> {
            programScreen.close();
            new LoginScreen();
        });

        bp = new BorderPane();

        Scene sc = new Scene(bp,sceneWidth,sceneHeight);
        sc.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        //CSS lisamiseks sain abi:
        //http://stackoverflow.com/questions/16236641/javafx-add-dynamically-css-files,
        //http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introscenegraph

        programScreen.setScene(sc);
        programScreen.show();
    }

    //Programmi menüü loomine
    private void programMenu () {

        //Menüü loomine JavaFX GUI Tutorial videode abil:
        //https://www.youtube.com/watch?v=JBJ9MIEfU3k&index=21&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
        //https://www.youtube.com/watch?v=AP4e6Lxncp4&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG&index=22

        //Menüüpunktide loomine ja nende tegevuste seadistamine
        Menu fileMenu = new Menu("_File");

        MenuItem newPurchase = new MenuItem("New purchase");    //Valik avab ostu sisestamise akna
        fileMenu.getItems().add(newPurchase);
        newPurchase.setOnAction(event -> toCostInputScreen());

        MenuItem query = new MenuItem("Query");                 //Valik avab ostu paringute akna
        fileMenu.getItems().add(query);
        query.setOnAction(event -> toQueryScreen());

        MenuItem logout = new MenuItem("Log out");              //Valik logib valja - LoginScreenile
        fileMenu.getItems().add(logout);
        logout.setOnAction(event -> logoutUser());

        Menu viewMenu = new Menu ("_View");                     //Valik n2itab antud ekraanil erinevate perioodide kulutusi
        viewPeriod = new ToggleGroup();
        view3 = new RadioMenuItem("View last 3 months");
        view6 = new RadioMenuItem("View last 6 months");
        view6.setSelected(true);                                //Vaikimisi on valitud perioodiks 6 kuud
        view12 = new RadioMenuItem("View last 12 months");

        view3.setToggleGroup(viewPeriod);
        view6.setToggleGroup(viewPeriod);
        view12.setToggleGroup(viewPeriod);

        viewMenu.getItems().addAll(view3, view6, view12);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, viewMenu);

        bp.setTop(menuBar);

    }

    //Meetod kuvab ekraanile kasutaja poolt (View menüüst) valitud perioodi kulutused tabelina ja graafikuna
    private void showMainTableAndGraph() {
        VBox v = new VBox();
        v.setPadding(new Insets(20,20,20,20));
        Databases db = new Databases();
        Graphs graph = new Graphs();
        Tables table = new Tables();
        Text noPurchase = new Text("There are no purchases inserted: nothing to see yet.");
        months = periodLength();
        heading = new Text("Costs in last " +months+ " months by categories");
        heading.setFont(Font.font("Calibri", 20));

        //kontrollib, kas tabelis eksisteerib oste: kui jah, siis kuvatakse tabel+graafik.
        if (!db.checkPurchaseExistance()) {
            v.getChildren().add(noPurchase);
        } else {
            v.getChildren().addAll(heading, table.amountLastMonthsByCategories(periodLength()), graph.amountLastMonthsByCategories(periodLength()));
        }

        v.setAlignment(Pos.TOP_CENTER);
        bp.setAlignment(v, Pos.TOP_CENTER);
        bp.setCenter(v);

        //Iga kasutaja valiku puhul View menüüs kuvatakse ekraanile uue perioodi vaade
        view12.setOnAction(event -> {
            showMainTableAndGraph();
        });
        view6.setOnAction(event -> {
            showMainTableAndGraph();
        });
        view3.setOnAction(event -> {
            showMainTableAndGraph();
        });

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

    //Meetod määrab perioodi pikkuse vastavalt kasutaja valikule View menüüs
    private int periodLength() {
        months = 0;
        if(view12.isSelected()){
            months = 12;
        }
        if(view6.isSelected()){
            months = 6;
        }
        if(view3.isSelected()){
            months = 3;
        }
        return months;
    }

}


