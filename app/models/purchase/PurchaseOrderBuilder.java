package models.purchase;

import models.delivery.Delivery;
import models.delivery.DeliveryFactory;
import models.payment.CreditCard;
import models.payment.Payment;
import models.product.ProductOrder;
import models.users.Customer;
import models.users.UserInfo;

import java.util.List;

public class PurchaseOrderBuilder {
    //String delivery;
    List<ProductOrder> productOrders;
    UserInfo customer;

    public PurchaseOrderBuilder(String delivery, List<ProductOrder> productOrderList,
                                UserInfo customer) {
        //this.delivery = delivery;
        this.productOrders = productOrderList;
        this.customer = customer;
    }

    /*private Delivery buildDelivery() {
        //DeliveryFactory deliveryFactory = new DeliveryFactory();
        //Delivery deliveryMethod = deliveryFactory.getDelivery(delivery);
        Delivery deliveryMethod = new Delivery(delivery);
        deliveryMethod.save();
        return deliveryMethod;
    }*/

    private void updateProductOrderRelations(PurchaseOrder purchaseOrder) {
        for (ProductOrder productOrder : productOrders) {
            productOrder.purchaseOrder = purchaseOrder;
            productOrder.update();
        }
    }

    public PurchaseOrder build() {
        // create Delivery object
        //Delivery deliveryMethod = buildDelivery();

        // create purchase order object
        //TODO: build purchasorderv2
        PurchaseOrder purchaseOrder = new PurchaseOrder(customer, null, productOrders);
        purchaseOrder.save();

        // Update reverse relations of purchase orders
        updateProductOrderRelations(purchaseOrder);

        // return purchase order
        return purchaseOrder;
    }
}
