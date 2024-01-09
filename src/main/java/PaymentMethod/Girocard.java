package PaymentMethod;

import Interfaces.Payment;

public class Girocard implements Payment {
    private double amount;
    @Override
    public void pay(double amount) {
        this.amount = amount;
    }

    @Override
    public String transactionDetails() {
        return "Du hast "+ amount+ "€ mit Girokarte gezahlt";
    }
}
