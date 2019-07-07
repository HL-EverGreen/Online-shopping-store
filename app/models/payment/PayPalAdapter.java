package models.payment;

import models.users.Customer;

public class PayPalAdapter implements PaymentService {
    private PayPalService payPalService;
    private CreditCard creditCard;
    private Customer customer;

    public PayPalAdapter(Customer customer, CreditCard creditCard) {
        payPalService = new PayPalService();
        this.customer = customer;
        this.creditCard = creditCard;
    }

    @Override
    public void pay() {
        // Create data compatible for paypal servie
        PayPalInfo payPalInfo = new PayPalInfo();
        payPalInfo.creditCardNumber = creditCard.creditCardNumber;
        payPalInfo.cvv = creditCard.cvv;
        payPalInfo.expiryMonth = creditCard.expiryMonth;
        payPalInfo.expiryYear = creditCard.expiryYear;
        payPalInfo.emailID = customer.emailID;

        payPalService.makePayment(payPalInfo);
    }
}
