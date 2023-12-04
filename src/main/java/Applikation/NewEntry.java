package Applikation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewEntry {
    private String datum;
    private String grund;
    private Double betrag;
    private Double kontostand;
    private Integer wichtigkeit;

    public String user;
    public static Logger log = LogManager.getLogger(NewEntry.class);

    //constructor:
    public NewEntry(String datum, String grund, Double betrag, Double kontostand, Integer wichtigkeit) {
        this.datum = datum;
        this.grund = grund;
        this.betrag = betrag;
        this.kontostand = kontostand;
        this.wichtigkeit = wichtigkeit;
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

    public Integer getWichtigkeit() {return wichtigkeit;}


}
