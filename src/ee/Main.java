package ee;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Maila on 24/11/2015.
 */
public class Main extends Application { //Miks mul on eraldi screenid, mitte eraldi stseenid?

    @Override
    public void start(Stage primaryStage) throws Exception {
        new LoginScreen();

    }
}
