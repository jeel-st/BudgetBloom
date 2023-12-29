package Logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogicTableEntry {
    private String date;
    private String reason;
    private Double amount;
    private Double accountBalance;
    private Integer importance;
    private String regularity;
    public String user;
    public static Logger log = LogManager.getLogger(LogicTableEntry.class);

    //constructor:
    public LogicTableEntry(String date, String reason, Double amount, Double accountBalance, Integer importance, String regularity) {
        this.date = date;
        this.reason = reason;
        this.amount = amount;
        this.accountBalance = accountBalance;
        this.importance = importance;
        this.regularity = regularity;
    }

    //Getters:


    public String getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public Integer getImportance() {return importance;}

    public String getRegularity(){return regularity;}
}
