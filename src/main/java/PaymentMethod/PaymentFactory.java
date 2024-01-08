package PaymentMethod;

public class PaymentFactory {
    public Payment createCreditcard(){
        return new Creditcard();
    }
    public Payment createCash(){
        return new Cash();
    }
    public Payment createPaypal(){
        return new PayPal();
    }
    public Payment createGirocard(){return new Girocard(); }
    public Payment createOtherPayment(){return new OtherPayment();}

}
