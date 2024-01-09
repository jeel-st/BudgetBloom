package PaymentMethod;

import Interfaces.Payment;

public class Cash implements Payment {
    private double amount;
    @Override
    public void pay(double amount) {
        this.amount = amount;
    }

    @Override
    public String transactionDetails() {
        return "Du hast "+ amount+ "€ Bar gezahlt";
    }
}
