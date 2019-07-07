package models.purchase;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PurchaseOrderScheduler {
    PurchaseOrderScheduler scheduler = null;

    public PurchaseOrderScheduler getInstance(){
        if (scheduler == null) {
            scheduler = new PurchaseOrderScheduler();
        }

        return scheduler;
    }

    /**
     * PriorityQueue to queue purchase orders based on priority
     */
    PriorityQueue<PurchaseOrderRequest> purchaseOrderRequests = new PriorityQueue<>(new Comparator<PurchaseOrderRequest>() {
        @Override
        public int compare(PurchaseOrderRequest o1, PurchaseOrderRequest o2) {
            if (o1 instanceof SpecialPurchaseOrderRequest) {
                return -1;
            } else {
                return 1;
            }
        }
    });

    public void addRequest(PurchaseOrderRequest purchaseOrderRequest) {
        purchaseOrderRequests.add(purchaseOrderRequest);
    }

    public void execute() {
        while (!purchaseOrderRequests.isEmpty()) {
            PurchaseOrderRequest purchaseOrderRequest = purchaseOrderRequests.poll();
            purchaseOrderRequest.checkout();
        }
    }
}
