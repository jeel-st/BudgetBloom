package PaymentMethod;

public class PayPal implements Payment{
    @Override
    public void pay(double amount) {

    }
    @Override
    public String transactionDetails(){
        return "";
    }
}
