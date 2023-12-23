package Logic;

import java.sql.SQLException;
import java.time.LocalDate;

public class LogicFacade {

    private static LogicFacade instance;
    private final LogicEditEntry editEntry;
    private final LogicBalance balance;
    private final LogicDatabase database;
    private final LogicOverview overview;
    private final LogicRegister register;
    private final LogicNewEntry newEntry;
    //private LogicTableEntry tableEntry;


    private LogicFacade(){
        editEntry = new LogicEditEntry();
        balance = new LogicBalance();
        database = new LogicDatabase();
        overview = new LogicOverview();
        register = new LogicRegister();
        newEntry = new LogicNewEntry();
        // ??? tableEntry = new LogicTableEntry();
    }

    public static LogicFacade getInstance() {
        if (instance == null){
            instance = new LogicFacade();
        }
        return instance;
    }

    public String saveEdit(LocalDate eingabeDatum, String eingabeGrund, int skala, String repeatBox, String eingabeZahl, String myChoiceBox, String wiederholungshaeufigkeitBox) {
           return editEntry.saveEdit(eingabeDatum, eingabeGrund, skala, repeatBox, eingabeZahl, myChoiceBox, wiederholungshaeufigkeitBox);
    }

    public double kontoVeränderungsÜberprüfer(String eingabeZahl, String myChoiceBox) {
        return newEntry.kontoVeränderungsÜberprüferEdit(eingabeZahl, myChoiceBox);
    }

    public String showContentOfWiederholungshaeufigkeitBox() throws Exception, SQLException {
        return editEntry.showContentOfWiederholungshaeufigkeitBox();
    }

}
