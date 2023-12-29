package Interfaces;

import javafx.event.ActionEvent;

import java.io.IOException;

public interface EntryInterface {

    void userCancel(ActionEvent event)throws IOException;

    void getRepeat(ActionEvent event);

    void getInput(ActionEvent event);


}
