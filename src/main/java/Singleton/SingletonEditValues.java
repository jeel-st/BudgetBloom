package Singleton;

public class SingletonEditValues {
    private static SingletonEditValues instance;

    private String date;
    private String note;
    private double bankbalance;
    private double amount;
    private int importance;
    private String isRegular;


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
    public String getIsregular() { return isRegular; }
    public double getBankbalance() { return bankbalance; }
    public double getAmount() { return amount; }
    public int getImportance() { return importance; }


    public void setDate(String date) {
        this.date = date;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public void setIsregular(String isregualar) {
        this.isRegular = isregualar;
    }
    public void setBankbalance(double bankbalance) {
        this.bankbalance = bankbalance;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setImportance(int importance) {
        this.importance = importance;
    }
}
