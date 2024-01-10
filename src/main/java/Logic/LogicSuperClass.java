package Logic;

import Interfaces.Payment;
import PaymentMethod.PaymentFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogicSuperClass {
    public static Logger log = LogManager.getLogger(LogicSuperClass.class);
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

    String checkPayment(String payment, double amountChange){
        PaymentFactory pf = new PaymentFactory();
        switch(payment) {
            case "Bar" -> {
                Payment p = pf.createCash();
                p.pay(amountChange);
                log.info(p.transactionDetails());
                return payment;
            }
            case "Paypal" -> {
                Payment p = pf.createPaypal();
                p.pay(amountChange);
                log.info(p.transactionDetails());
                return payment;
            }
            case "Kreditkarte" -> {
                Payment p = pf.createCreditcard();
                p.pay(amountChange);
                log.info(p.transactionDetails());
                return payment;
            }
            case "Girokarte" -> {
                Payment p = pf.createGirocard();
                p.pay(amountChange);
                log.info(p.transactionDetails());
                return payment;
            }
            case "weitere Zahlungsmethode..." -> {
                Payment p = pf.createOtherPayment();
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
