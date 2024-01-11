package Logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogicTableEntry {
    private final String date;
    private final String reason;
    private final Double amount;
    private final Double accountBalance;
    private final Integer importance;
    private final String regularity;
    private final String payment;

    public static Logger log = LogManager.getLogger(LogicTableEntry.class);

    //constructor:
    public LogicTableEntry(String date, String reason, Double amount, Double accountBalance, Integer importance, String regularity, String payment) {
        this.date = date;
        this.reason = reason;
        this.amount = amount;
        this.accountBalance = accountBalance;
        this.importance = importance;
        this.regularity = regularity;
        this.payment = payment;
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
    public String getPayment(){return payment;}
}
