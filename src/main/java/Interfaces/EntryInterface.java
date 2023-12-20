package Interfaces;

import javafx.event.ActionEvent;

import java.io.IOException;

public interface EntryInterface {

    void userAbbruch(ActionEvent event)throws IOException;

    void getRepeat(ActionEvent event);

    void getEingabe(ActionEvent event);


}
