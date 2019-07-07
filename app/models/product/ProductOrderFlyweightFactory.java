package models.product;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight factory for product orders
 */
public class ProductOrderFlyweightFactory {
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
}
