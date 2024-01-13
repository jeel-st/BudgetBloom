package mainpackage;

import Threads.BalanceThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Driver extends Application {
    private static Stage stg;
    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        stage.setResizable(false);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/sample.fxml")));
        stage.setTitle("BudgetBloom");
        stage.setScene(new Scene(root, 600,400));
        stage.show();
    }

    public void changeScene(String fxml) throws IOException {
        FXMLLoader scene = new FXMLLoader(getClass().getResource(fxml));
        Parent pane = scene.load();
        scene.getController();
        //auskommentiert, da es zum zweiter Aufruf von Initialize führt
        //Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stg.getScene().setRoot(pane);
    }
    public static Logger log = LogManager.getLogger(Driver.class);
    public static void main(String[] args){
        log.info("Programm started");
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        // Startet den Thread sofort und führe ihn jede Minute aus
        executor.scheduleAtFixedRate(new BalanceThread(), 0, 1, TimeUnit.MINUTES);

        launch(args);

        executor.shutdown();


    }
    
}
