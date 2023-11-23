package Applikation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewEntry {
    private String datum;
    private String grund;
    private Double betrag;
    private Double kontostand;
    public String user;
    public static Logger log = LogManager.getLogger(NewEntry.class);

    //constructor:
    public NewEntry(String datum, String grund, Double betrag, Double kontostand) {
        this.datum = datum;
        this.grund = grund;
        this.betrag = betrag;
        this.kontostand = kontostand;
    }

    //Getters:


    public String getDatum() {
        return datum;
    }

    public String getGrund() {
        return grund;
    }

    public Double getBetrag() {
        return betrag;
    }

    public Double getKontostand() {
        return kontostand;
    }


}
