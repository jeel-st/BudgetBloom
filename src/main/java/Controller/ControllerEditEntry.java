package Controller;

import Interfaces.EntryInterface;
import Logic.LogicDatabase;
import Logic.LogicEditEntry;
import Logic.LogicFacade;
import Singleton.SingletonEditValues;
import Singleton.SingletonUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import mainpackage.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerEditEntry implements Initializable, EntryInterface {

    @FXML
    private Label errorLabel;

    @FXML
    private Button cancel;

    @FXML
    private DatePicker inputDate;

    @FXML
    private TextField inputReason;

    @FXML
    private Button inputSave;

    @FXML
    private Label inputText;

    @FXML
    private TextField inputNumber;

    @FXML
    private ChoiceBox<String> myChoiceBox;

    @FXML
    private Slider scale;

    @FXML
    private Label scaleLabel;

    @FXML
    private ChoiceBox<String> repeatBox;
    @FXML
    private ChoiceBox<String> repeatabilityBox;
    @FXML
    private ChoiceBox<String> paymentMethodBox;
    private String[] paymentArr = {"Bar", "Paypal", "Kreditkarte", "Girokarte", "weitere Zahlungsmethode..."};
    private String[] repetitions = {"Einmalig", "Regelmäßig"};
    private String[] repeatability = {"täglich", "monatlich", "jährlich"};
    int myScaleNumber;
    private String[] input = {"Einnahme", "Ausgabe"};
    SingletonEditValues sev = SingletonEditValues.getInstance();
    LogicEditEntry lee = new LogicEditEntry();
     String date = sev.getDate();
     String note = sev.getNote();
     double amount = sev.getAmount();
     int importance = sev.getImportance();
     String isRegular = sev.getIsRegular();
     String payment = sev.getPayment();
    Driver d = new Driver();

    SingletonUser sp = SingletonUser.getInstance();

    public static Logger log = LogManager.getLogger(ControllerEditEntry.class);

    @FXML
    public void userCancel(ActionEvent event) throws IOException {
        d.changeScene("/FXML/overview.fxml");
    }

    @FXML
    void userInputSave(ActionEvent event) throws SQLException, IOException {

        if (repeatBox.getValue().equals("Regelmäßig") && repeatabilityBox.getValue() == null) {
            log.warn("Enter a frequency");
        } else {
            if (LogicFacade.getInstance().isRegularBool(inputNumber.getText())) {
                String errorLabelText = lee.saveEdit(inputDate.getValue(), inputReason.getText(), (int) scale.getValue(), repeatBox.getValue(), inputNumber.getText(), myChoiceBox.getValue(), repeatabilityBox.getValue(), paymentMethodBox.getValue());
                errorLabel.setText(errorLabelText);
                if (Objects.equals(errorLabelText, "Edit was saved successfully")) {
                    d.changeScene("/FXML/overview.fxml");
                }
            } else {
                log.warn("Enter a number in the given format");
                errorLabel.setText("Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!");
            }
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {

        repeatBox.getItems().addAll(repetitions);
        repeatBox.setOnAction(this::getRepeat);

        // bis hier stehen lassen

        if (isRegular.equals("Einmalig")) {
            repeatBox.setValue(isRegular);
            repeatabilityBox.setVisible(false);
        } else {
            repeatBox.setValue(isRegular);
            repeatabilityBox.setVisible(true);
            try {
                repeatabilityBox.setValue(lee.showContentOfRepeatabilityBox());
            } catch (Exception e) {
                log.error("Content does not fit into repeatabilityBox");
            }
        }
        repeatabilityBox.getItems().addAll(repeatability);


        try {
            if (amount >= 0) {
                myChoiceBox.setValue("Einnahme");
                paymentMethodBox.setVisible(false);
            } else {
                myChoiceBox.setValue("Ausgabe");
                paymentMethodBox.setVisible(true);
                if(payment != null) {
                    paymentMethodBox.setValue(payment);
                }else{
                    paymentMethodBox.setValue("Bar");
                }
            }

        } catch (Exception e) {
            log.error("Something went wrong with set from 'myChoiceBox' ");
        }
        inputDate.setValue(LocalDate.parse(date));
        inputReason.setText(note);
        inputNumber.setText(String.valueOf(amount));
        scale.setValue(importance);


        //ChoiceBox:
        myChoiceBox.getItems().addAll(input);
        myChoiceBox.setOnAction(this::getInput);  //this:: ist ein reverence operator (zum Label)
        paymentMethodBox.getItems().addAll(paymentArr);

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
            public void getRepeat (ActionEvent event){
                String repetition = repeatBox.getValue();
                if ("Regelmäßig".equalsIgnoreCase(repetition)) {
                    repeatabilityBox.setVisible(true);
                } else {
                    repeatabilityBox.setVisible(false);
                }
            }
            public void getInput(ActionEvent event){
                String myInput = myChoiceBox.getValue();
                inputText.setText("Neue " + myInput + ":");
                paymentMethodBox.setVisible("Ausgabe".equalsIgnoreCase(myInput));
                if(paymentMethodBox.isVisible()){
                    if(payment != null) {
                        paymentMethodBox.setValue(payment);
                    }else{
                        paymentMethodBox.setValue("Bar");
                    }
                }
            }

    }

