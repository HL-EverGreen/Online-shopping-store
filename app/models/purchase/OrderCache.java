package models.purchase;

import models.PrototypeCapable;
import models.ShoppingCart;
import models.delivery.Delivery;
import models.product.ProductOrder;
import models.users.UserInfo;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
//import models.PrototypeCapable;

public class OrderCache {
    private static Map<String, PrototypeCapable> prototypes = new HashMap<>();

    public Map<String, PrototypeCapable> getPrototypeMap() {
        return prototypes;
    }

    public static PrototypeCapable getInstance(String s, String delivery, List<ProductOrder> productOrderList, UserInfo customer) throws CloneNotSupportedException {
    //public static PrototypeCapable getInstance(String s, PurchaseOrder purchaseOrder) throws CloneNotSupportedException {
        String key = s;
        if (prototypes.containsKey(key)) {
            return ((PrototypeCapable) prototypes.get(s).clone());
        }
        // Build Purchase order
        PurchaseOrderBuilder purchaseOrderBuilder = new PurchaseOrderBuilder(null,
                productOrderList, customer);
        PurchaseOrder purchaseOrder = purchaseOrderBuilder.build();
        prototypes.put(key, (PrototypeCapable) purchaseOrder);
        return (PrototypeCapable) purchaseOrder;
    }
}


/*
    private static Map<String, ProductOrder> productOrderMap = new HashMap<>();

    // Key for the map is a combo of productId, delivery and quantity
    public static ProductOrder getProductOrder(Long productId, String delivery, String quantity, String coupon) {
        String key = productId + ":" + delivery + ":" + quantity + ":" + coupon;

        if (productOrderMap.containsKey(key)) {
            return productOrderMap.get(key);
        }

        // create product order if does not exist
        ProductOrder productOrder = new ProductOrder();
        productOrder.productId = productId;
        productOrder.quantity = Integer.valueOf(quantity);
        productOrder.delivery = delivery;
        productOrder.coupon = coupon;

        productOrderMap.put(key, productOrder);
        return productOrder;
    }

 */