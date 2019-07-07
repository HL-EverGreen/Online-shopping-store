package models;

import com.avaje.ebean.Model;
import models.product.ProductOrder;
import models.purchase.Purchase;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import models.purchase.PurchaseOrder;
import java.util.List;

@Entity
public class PurchaseHistory extends Model {
    @Id
    public String userEmail;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "history")
    public List<PurchaseOrder> purchaseOrderList;

    public PurchaseHistory(){}

    public void addOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrderList.add(purchaseOrder);
        this.update();
        purchaseOrder.history = this;
        purchaseOrder.update();
    }

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String, PurchaseHistory> find = new Finder<>(PurchaseHistory.class);
}
