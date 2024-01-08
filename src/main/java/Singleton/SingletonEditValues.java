package Singleton;

public class SingletonEditValues {
    private static SingletonEditValues instance;

    private String date;
    private String note;
    private double accountBalance;
    private double amount;
    private int importance;
    private String isRegular;
    private String payment;

    private SingletonEditValues() {
        date = null;
    }  // Privater Konstruktor, um die Instanz zu verhindern


    public static SingletonEditValues getInstance() {
        if (instance == null) {
            instance = new SingletonEditValues();
        }
        return instance;
    }

    public String getDate() {
        return date;
    }
    public String getNote() { return note; }
    public String getIsRegular() { return isRegular; }
    public double getAccountBalance() { return accountBalance; }
    public double getAmount() { return amount; }
    public int getImportance() { return importance; }
    public String getPayment(){return payment;}


    public void setDate(String date) {
        this.date = date;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public void setIsRegular(String isRegular) {
        this.isRegular = isRegular;
    }
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setImportance(int importance) {
        this.importance = importance;
    }
    public void setPayment(String payment){this.payment = payment;}
}
