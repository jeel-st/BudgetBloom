package MiniProject;

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
}
