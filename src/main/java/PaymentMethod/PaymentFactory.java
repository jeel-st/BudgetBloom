package PaymentMethod;

import Interfaces.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class  PaymentFactory {
    private static final Logger log = LogManager.getLogger(PaymentFactory.class);

    public static Payment getInstance(String s) throws Exception {
        if(s.equals("Bar")){
            return new Cash();
        }else if(s.equals("Paypal")){
            return new PayPal();
        }else if(s.equals("Kreditkarte")){
            return new Creditcard();
        }else if(s.equals("Girokarte")){
            return new Creditcard();
        }else if(s.equals("weitere Zahlungsmethode...")){
            return new OtherPayment();
        }else{
            log.warn("You're payment wasn't accepted. Please use a given payment method");
            throw new Exception();
        }
    }

}
