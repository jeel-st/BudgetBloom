package Controller;
import Interfaces.EntryInterface;
import Exceptions.NewEntryExceptions.AmountChangeIsNullException;
import Exceptions.NewEntryExceptions.NoteIsNullException;
import Exceptions.NewEntryExceptions.ParseDateException;
import Exceptions.NewEntryExceptions.ParseDoubleException;
import Logic.LogicFacade;
import Logic.LogicNewEntry;
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
    private int myScaleNumber;
    @FXML
    private Slider scale;
    @FXML
    private ChoiceBox<String> repeatBox;
    @FXML
    private ChoiceBox<String> repeatabilityBox;
    @FXML
    private ChoiceBox<String> paymentMethodBox;

    private static final Logger log = LogManager.getLogger(ControllerNewEntry.class);
    private final String[] repetitions = {"Einmalig", "Regelmäßig"};
    private final String[] input = {"Einnahme", "Ausgabe"};
    private final String[] repeatability = {"täglich", "monatlich", "jährlich"};
    private final String[] payment = {"Bar", "Paypal", "Kreditkarte", "Girokarte", "weitere Zahlungsmethode..."};
    private final Driver d = new Driver();
    private final LogicNewEntry lne = new LogicNewEntry();
    private Boolean repeatBool;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBox.getItems().addAll(input);
        myChoiceBox.setOnAction(this::getInput);
        myChoiceBox.setValue("Einnahme");
        repeatBox.getItems().addAll(repetitions);
        repeatBox.setOnAction(this::getRepeat);
        repeatBox.setValue("Einmalig");
        repeatabilityBox.getItems().addAll(repeatability);
        repeatabilityBox.setVisible(false);
        paymentMethodBox.getItems().addAll(payment);
        paymentMethodBox.setVisible(false);

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

    public void userInputAdd(ActionEvent event) throws Exception {
            repeatBool = LogicFacade.getInstance().isRegularBool(repeatBox.getValue());
            int sliderValue = (int) scale.getValue();
            boolean rightFormat = false;
            try{
                if(LogicFacade.getInstance().checkingFormats(inputNumber.getText(), inputReason.getText(), inputDate.getValue())){
                    rightFormat = true;
                }
            } catch (NoteIsNullException e) {
                log.warn("note is empty");
                errorLabel.setText("Bitte fügen Sie einen Grund hinzu!");
            } catch (ParseDoubleException e) {
                log.warn("format is false");
                errorLabel.setText("Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!");
            } catch (ParseDateException e) {
                log.warn("date isn't set");
                errorLabel.setText("Bitte fügen Sie ein Datum hinzu!");
            } catch (AmountChangeIsNullException e){
                log.warn("Field for income is empty");
                errorLabel.setText("Das Feld für ihre Einnahmen/Ausgaben ist ein Pflichtfeld");
            }
        if (rightFormat) {
            if (repeatabilityBox.getValue() != null && repeatBool || repeatabilityBox.getValue() == null && !repeatBool) {

                lne.changedAccount((Double.parseDouble(inputNumber.getText())), myChoiceBox.getValue(), sliderValue, inputReason.getText(), Date.valueOf((inputDate.getValue())), repeatabilityBox.getValue(), repeatBox.getValue(), paymentMethodBox.getValue());

                //Direkten Klassenaufruf auskommentieren und den Aufruf, der über die LogicFacade statt findet benutzen, der im Moment auskommentiert ist.
                //LogicFacade.getInstance().changedAccount((Double.parseDouble(inputNumber.getText())), myChoiceBox.getValue(), sliderValue, inputReason.getText(), Date.valueOf((inputDate.getValue())), repeatabilityBox.getValue(), repeatBox.getValue(), paymentMethodBox.getValue());

                d.changeScene("/FXML/overview.fxml");
            } else {
                log.warn("Geben sie an, wie oft die Ausgabe/Einnahme wiederholt werden soll");
                errorLabel.setText("Geben sie an, wie oft die Ausgabe/Einnahme wiederholt werden soll");
            }
        }
    }
}

