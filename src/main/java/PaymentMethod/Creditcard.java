package PaymentMethod;

import Interfaces.Payment;

public class Creditcard implements Payment {
    private double amount;
    @Override
    public void pay(double amount) {
        this.amount = amount;
    }
    @Override
    public String transactionDetails(){
        return "Du hast "+ amount+ "€ mit Kreditkarte gezahlt";
    }
}
