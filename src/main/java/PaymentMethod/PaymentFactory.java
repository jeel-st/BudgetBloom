package PaymentMethod;

import Interfaces.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class  PaymentFactory {
    private static final Logger log = LogManager.getLogger(PaymentFactory.class);

    public static Payment getInstance(String paymentType) throws Exception {
        switch (paymentType) {
            case "Bar" -> {
                return new Cash();
            }
            case "Paypal" -> {
                return new PayPal();
            }
            case "Kreditkarte", "Girokarte" -> {
                return new Creditcard();
            }
            case "weitere Zahlungsmethode..." -> {
                return new OtherPayment();
            }
            default -> {
                log.warn("You're payment wasn't accepted. Please use a given payment method");
                throw new Exception();
            }
        }
    }

}
