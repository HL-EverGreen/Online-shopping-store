package models.product;

import com.avaje.ebean.Model;
import com.sun.javafx.beans.IDProperty;
import models.Inventory;
import models.ShoppingCart;
import models.delivery.Delivery;
import models.productStatus.ProductStatus;
import models.purchase.PurchaseOrder;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProductOrder extends Model {
    @Id
    @GeneratedValue
    public Long id;

    //@OneToOne
    public Long productId;

    //@ManyToOne
    public String delivery;

    public String coupon;

    // TODO: save reverse relations
    @ManyToOne
    public PurchaseOrder purchaseOrder;

    @ManyToOne
    public ShoppingCart shoppingCart;

    @Constraints.Required
    public int quantity;

    @ManyToOne
    public Inventory inventory;

    @ManyToOne
    public ProductStatus status;

    public void setStatus(ProductStatus status) {
        if(this.status != null) {
            for(int i = 0; i < this.status.productList.size(); i++) {
                if(this.status.productList.get(i).id.toString().equals(this.id.toString())) {
                    this.status.productList.remove(i);
                    this.status.update();
                    this.status = null;
                    break;
                }
            }
        }
        this.status = status;
        status.productList.add(this);
        status.update();
        this.update();
    }

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Find<Long,ProductOrder> find = new Find<Long,ProductOrder>(){};
}
