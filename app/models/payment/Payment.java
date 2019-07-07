package models.payment;

import models.users.Customer;

public class Payment implements PaymentService {
    private PayPalAdapter payPalAdapter;

    public Payment(Customer customer, CreditCard card) {
        payPalAdapter = new PayPalAdapter(customer, card);
    }

    @Override
    public void pay() {
        payPalAdapter.pay();
    }
}
