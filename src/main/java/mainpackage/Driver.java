package mainpackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


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

    public void changeScene(String fxml) throws IOException{
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stg.getScene().setRoot(pane);
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
        //Controller controller = new Controller();
        //Kontodaten kontodaten = new Kontodaten();
        //kontodaten.Kontoabfrage();
        //controller.controller();

    }


}
