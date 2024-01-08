package Controller;
import Interfaces.EntryInterface;
import LocalExceptions.NewEntryExceptions.AmountChangeIsNullException;
import LocalExceptions.NewEntryExceptions.NoteIsNullException;
import LocalExceptions.NewEntryExceptions.ParseDateException;
import LocalExceptions.NewEntryExceptions.ParseDoubleException;
import Logic.LogicDatabase;
import Logic.LogicFacade;
import Singleton.SingletonUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mainpackage.Driver;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


import javafx.scene.control.Button;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControllerNewEntry implements Initializable, EntryInterface {

    @FXML
    private Label inputText;

    @FXML
    private Label errorLabel;

    @FXML
    private DatePicker inputDate;

    @FXML
    private TextField inputReason;

    @FXML
    private ChoiceBox<String> myChoiceBox;

    @FXML
    private TextField inputNumber;

    @FXML
    private Label scaleLabel;

    int myScaleNumber;

    @FXML
    private Slider scale;

    @FXML
    private Button inputAdd;

    @FXML
    private Button cancel;
    @FXML
    private ChoiceBox<String> repeatBox;
    @FXML
    private ChoiceBox<String> repeatabilityBox;
    @FXML
    private ChoiceBox<String> paymentMethodBox;

    public static Logger log = LogManager.getLogger(ControllerNewEntry.class);

    private String[] repetitions = {"Einmalig", "Regelmäßig"};
    //kommt in die choicebox:
    private String[] input = {"Einnahme", "Ausgabe"};
    private String[] repeatability = {"täglich", "monatlich", "jährlich"};
    private String[] payment = {"Bar", "Paypal", "Kreditkarte", "Girokarte", "weitere Zahlungsmethode..."};
    Driver d = new Driver();
    LogicDatabase dc = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    private String localUsername = sp.getName();
    private Boolean repeatBool;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //ChoiceBox:
        myChoiceBox.getItems().addAll(input);
        myChoiceBox.setOnAction(this::getInput);  //this:: ist ein reverence operator (zum Label)
        myChoiceBox.setValue("Einnahme");
        repeatBox.getItems().addAll(repetitions);
        repeatBox.setOnAction(this::getRepeat);
        repeatBox.setValue("Einmalig");
        repeatabilityBox.getItems().addAll(repeatability);
        repeatabilityBox.setVisible(false);
        paymentMethodBox.getItems().addAll(payment);
        paymentMethodBox.setVisible(false);

        //WichtigkeitsSkala (Slider + Label):
        myScaleNumber = (int) scale.getValue();
        scaleLabel.setText(Integer.toString(myScaleNumber));
        scale.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                myScaleNumber = (int) scale.getValue();
                scaleLabel.setText(Integer.toString(myScaleNumber));

            }
        });
    }
    public void getRepeat(ActionEvent event){
        String repetition = repeatBox.getValue();
        repeatabilityBox.setVisible("Regelmäßig".equalsIgnoreCase(repetition));
    }
    //Label verknüpfen:
    public void getInput(ActionEvent event) {
        String myInput = myChoiceBox.getValue();
        inputText.setText("Neue " + myInput + ":");
        paymentMethodBox.setVisible("Ausgabe".equalsIgnoreCase(myInput));
        if(paymentMethodBox.isVisible()){
            paymentMethodBox.setValue("Bar");
        }
    }


    public void userCancel(ActionEvent event) throws IOException {
        d.changeScene("/FXML/overview.fxml");
    }


    public void userInputAdd(ActionEvent event) throws SQLException, IOException {
            repeatBool = LogicFacade.getInstance().isRegularBool(repeatBox.getValue());
            int sliderValue = (int) scale.getValue();
            boolean rightFormat = false;
            try{
                if(LogicFacade.getInstance().checkingFormats(inputNumber.getText(), inputReason.getText(), inputDate.getValue())){
                    rightFormat = true;
                }
            } catch (NoteIsNullException e) {
                errorLabel.setText("Bitte fügen Sie einen Grund hinzu!");
            } catch (ParseDoubleException e) {
                errorLabel.setText("Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!");
            } catch (ParseDateException e) {
                errorLabel.setText("Bitte fügen Sie ein Datum hinzu!");
            } catch (AmountChangeIsNullException e){
                errorLabel.setText("Das Feld für ihre Einnahmen/Ausgaben ist ein Pflichtfeld");
            }
        if (rightFormat) {
            if (repeatabilityBox.getValue() != null && repeatBool || repeatabilityBox.getValue() == null && !repeatBool) {
                LogicFacade.getInstance().changedAccount((Double.parseDouble(inputNumber.getText())), myChoiceBox.getValue(), sliderValue, inputReason.getText(), Date.valueOf((inputDate.getValue())), repeatabilityBox.getValue(), repeatBox.getValue(), paymentMethodBox.getValue());
                d.changeScene("/FXML/overview.fxml");
            } else {
                log.error("Geben sie an, wie oft die Ausgabe/Einnahme wiederholt werden soll");
                errorLabel.setText("Geben sie an, wie oft die Ausgabe/Einnahme wiederholt werden soll");
            }
        }
        }


}

