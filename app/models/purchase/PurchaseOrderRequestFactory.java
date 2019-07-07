package models.purchase;

public class PurchaseOrderRequestFactory {
    public PurchaseOrderRequest getPurchaseOrderRequest(String request, PurchaseOrder purchaseOrder) {
        if (request.equalsIgnoreCase("special")) {
            return new SpecialPurchaseOrderRequest(purchaseOrder);
        } else {
            return new NormalPurchaseOrderRequest(purchaseOrder);
        }
    }
}
