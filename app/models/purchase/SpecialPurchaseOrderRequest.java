package models.purchase;

public class SpecialPurchaseOrderRequest implements PurchaseOrderRequest {

    private PurchaseOrder purchaseOrder;

    public SpecialPurchaseOrderRequest(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @Override
    public void checkout() {
        purchaseOrder.checkout();
    }
}
