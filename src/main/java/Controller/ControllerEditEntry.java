package Controller;

import Interfaces.EntryInterface;
import Logic.LogicDatabase;
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
    private String[] repetitions = {"Einmalig", "Regelmäßig"};
    private String[] repeatability = {"täglich", "monatlich", "jährlich"};
    int myScaleNumber;
    private String[] input = {"Einnahme", "Ausgabe"};
    SingletonEditValues sev = SingletonEditValues.getInstance();

    public String date = sev.getDate();
    public String note = sev.getNote();
    public double bankBalance = sev.getAccountBalance();
    public double amount = sev.getAmount();
    public int importance = sev.getImportance();
    public String isRegular = sev.getIsregular();
    Driver d = new Driver();
    LogicDatabase dc = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    private final String localUsername = sp.getName();
    public static Logger log = LogManager.getLogger(ControllerEditEntry.class);

    @FXML
    public void userCancel(ActionEvent event) throws IOException {
        d.changeScene("/FXML/overview.fxml");
    }

    @FXML
    void userInputSave(ActionEvent event) throws SQLException, IOException {
        ControllerNewEntry c = new ControllerNewEntry();
        if (repeatBox.getValue().equals("Regelmäßig") && repeatabilityBox.getValue() == null) {
            log.error("Geben sie eine Frequenz an");
        } else {
            if (LogicFacade.getInstance().checkIsRegularBoolean(inputNumber.getText())) {
                String errorLabelText = LogicFacade.getInstance().saveEdit(inputDate.getValue(), inputReason.getText(), (int) scale.getValue(), repeatBox.getValue(), inputNumber.getText(), myChoiceBox.getValue(), repeatabilityBox.getValue());
                errorLabel.setText(errorLabelText);
                if (Objects.equals(errorLabelText, "Edit was saved successfully")) {
                    d.changeScene("/FXML/overview.fxml");
                }
            } else {
                log.error("Geben sie eine Zahl in dem vorgegebenen Format an");
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
                repeatabilityBox.setValue(LogicFacade.getInstance().showContentOfRepeatabilityBox());
            } catch (Exception e) {
                log.error("Content passt nicht in WiederholungshäufigkeitsBox");
            }
        }
        repeatabilityBox.getItems().addAll(repeatability);

        try {
            if (amount >= 0) {
                myChoiceBox.setValue("Einnahme");
            } else {
                myChoiceBox.setValue("Ausgabe");
            }
            log.info("Date was set successfully");
        } catch (Exception e) {
            log.error("Date wasn't set");
        }
        inputDate.setValue(LocalDate.parse(date));
        inputReason.setText(note);
        inputNumber.setText(String.valueOf(amount));
        scale.setValue(importance);


        //ChoiceBox:
        myChoiceBox.getItems().addAll(input);
        myChoiceBox.setOnAction(this::getInput);  //this:: ist ein reverence operator (zum Label)


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
            }

    }

