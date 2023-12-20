package Logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogicTableEntry {
    private String datum;
    private String grund;
    private Double betrag;
    private Double kontostand;
    private Integer wichtigkeit;
    private String regelmäßigkeit;
    public String user;
    public static Logger log = LogManager.getLogger(LogicTableEntry.class);

    //constructor:
    public LogicTableEntry(String datum, String grund, Double betrag, Double kontostand, Integer wichtigkeit, String regelmäßigkeit) {
        this.datum = datum;
        this.grund = grund;
        this.betrag = betrag;
        this.kontostand = kontostand;
        this.wichtigkeit = wichtigkeit;
        this.regelmäßigkeit = regelmäßigkeit;
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

    public String getRegelmäßigkeit(){return regelmäßigkeit;}
}
