package PaymentMethod;

public class OtherPayment implements Payment{
    @Override
    public void pay(double amount) {

    }

    @Override
    public String transactionDetails() {
        return null;
    }
}
