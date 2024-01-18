package Logic;

import Interfaces.Payment;
import PaymentMethod.PaymentFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogicSuperClass {

    private static final Logger log = LogManager.getLogger(LogicSuperClass.class);

    public boolean isRegularBool(String s){
        return !s.equals("Einmalig");
    }

    public String checkFrequency(String repeatBox, String repeatabilityBox) {
        if (isRegularBool(repeatBox) && repeatabilityBox.equals("täglich")) {
            return "täglich";
        } else if (isRegularBool(repeatBox) && repeatabilityBox.equals("monatlich")) {
            return "monatlich";
        } else if (isRegularBool(repeatBox) && repeatabilityBox.equals("jährlich")) {
            return "jährlich";
        } else {
            return null;
        }
    }

    String checkPayment(String payment, double amountChange) throws Exception {
        String checknull = payment;
        if(payment == null){
            checknull = "-";
        }

        switch(checknull) {
            case "Bar" -> {
                Payment p = PaymentFactory.getInstance("Bar");
                p.pay(amountChange);
                log.info(p.transactionDetails());
                return payment;
            }
            case "Paypal" -> {
                Payment p = PaymentFactory.getInstance("Paypal");
                p.pay(amountChange);
                log.info(p.transactionDetails());
                return payment;
            }
            case "Kreditkarte" -> {
                Payment p = PaymentFactory.getInstance("Kreditkarte");
                p.pay(amountChange);
                log.info(p.transactionDetails());
                return payment;
            }
            case "Girokarte" -> {
                Payment p = PaymentFactory.getInstance("Girokarte");
                p.pay(amountChange);
                log.info(p.transactionDetails());
                return payment;
            }
            case "weitere Zahlungsmethode..." -> {
                Payment p = PaymentFactory.getInstance("weitere Zahlungsmethode...");
                p.pay(amountChange);
                log.info(p.transactionDetails());
                return payment;
            }
            default -> {
                return null;
            }
        }
    }
}
