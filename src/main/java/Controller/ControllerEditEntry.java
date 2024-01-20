package Controller;

import Interfaces.EntryInterface;
import Logic.LogicEditEntry;
import Logic.LogicFacade;
import Singleton.SingletonEditValues;
import Singleton.SingletonUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerEditEntry implements Initializable, EntryInterface {

    @FXML
    private Label errorLabel;
    @FXML
    private DatePicker inputDate;
    @FXML
    private TextField inputReason;
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
    private final String[] paymentArr = {"Bar", "Paypal", "Kreditkarte", "Girokarte", "weitere Zahlungsmethode..."};
    private final String[] repetitions = {"Einmalig", "Regelmäßig"};
    private final String[] repeatability = {"täglich", "monatlich", "jährlich"};
    private int myScaleNumber;
    private final String[] input = {"Einnahme", "Ausgabe"};
    private final SingletonEditValues sev = SingletonEditValues.getInstance();
    private final LogicEditEntry lee = new LogicEditEntry();
    private final String date = sev.getDate();
    private final String note = sev.getNote();
    private final double amount = sev.getAmount();
    private final int importance = sev.getImportance();
    private final String isRegular = sev.getIsRegular();
    private final String payment = sev.getPayment();
    private final Driver d = new Driver();
    private static final Logger log = LogManager.getLogger(ControllerEditEntry.class);

    @FXML
    public void userCancel(ActionEvent event) throws IOException {
        d.changeScene("/FXML/overview.fxml");
    }

    @FXML
    void userInputSave(ActionEvent event) throws Exception {
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
        if (isRegular.equals("Einmalig")) {
            repeatBox.setValue(isRegular);
            repeatabilityBox.setVisible(false);
        } else {
            repeatBox.setValue(isRegular);
            repeatabilityBox.setVisible(true);
            try {
                repeatabilityBox.setValue(lee.showContentOfRepeatabilityBox());
            } catch (Exception e) {
                log.error("Content does not fit into repeatabilityBox", e);
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
            log.error("Something went wrong with set from 'myChoiceBox' ", e);
        }
        inputDate.setValue(LocalDate.parse(date));
        inputReason.setText(note);
        inputNumber.setText(String.valueOf(amount));
        scale.setValue(importance);

        myChoiceBox.getItems().addAll(input);
        myChoiceBox.setOnAction(this::getInput);
        paymentMethodBox.getItems().addAll(paymentArr);

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

