package models.purchase;

import models.product.Product;
import models.payment.Payment;
import models.payment.CreditCard;
import models.users.Customer;

public class Purchase {
    Payment paymentSystem;

    public Purchase(Customer customer, CreditCard card) {
        paymentSystem = new Payment(customer, card);
    }

    public void doPurchase(Product product, CreditCard card) {
        // Make the payment using payment service
        paymentSystem.pay();
    }
}
