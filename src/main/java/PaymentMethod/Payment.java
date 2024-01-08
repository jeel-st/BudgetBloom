package PaymentMethod;

public interface Payment {
    void pay(double amount);
    String transactionDetails();
}
