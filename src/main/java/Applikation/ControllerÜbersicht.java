package Applikation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import mainpackage.Driver;

import java.io.IOException;

public class ControllerÜbersicht {
    @FXML
    private Button logout;

    public void userLogout(ActionEvent event)throws IOException {
        Driver d = new Driver();
        d.changeScene("/FXML/sample.fxml");
    }

}
