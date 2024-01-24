package Logic;

public class LogicTableEntry {
    private final String date;
    private final String reason;
    private final double amount;
    private final double accountBalance;
    private final int importance;
    private final String regularity;
    private final String payment;

    public LogicTableEntry(String date, String reason, double amount, double accountBalance, int importance, String regularity, String payment) {
        this.date = date;
        this.reason = reason;
        this.amount = amount;
        this.accountBalance = accountBalance;
        this.importance = importance;
        this.regularity = regularity;
        this.payment = payment;
    }

    public String getDate() {return date;}
    public String getReason() {return reason;}
    public double getAmount() {return amount;}
    public double getAccountBalance() {return accountBalance;}
    public int getImportance() {return importance;}
    public String getRegularity() {return regularity;}
    public String getPayment() {return payment;}
}
