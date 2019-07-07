package models.purchase;

public class NormalPurchaseOrderRequest implements PurchaseOrderRequest {

    private PurchaseOrder purchaseOrder;

    public NormalPurchaseOrderRequest(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @Override
    public void checkout() {
        purchaseOrder.checkout();
    }
}
