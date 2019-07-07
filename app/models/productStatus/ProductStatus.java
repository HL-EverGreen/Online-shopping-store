package models.productStatus;

import models.ShoppingCart;

import models.product.Product;
import models.product.ProductOrder;
import play.data.validation.Constraints;

import com.avaje.ebean.Model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import models.comment.Comment;
import models.notification.NotificationMessage;

import java.util.List;

@Entity
public class ProductStatus extends Model{
    // ordered
    // paid
    // delivered?
    @Id
    public String state;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "status")
    public List<ProductOrder> productList;

    public ProductStatus(){}

    public void setStatus(ProductOrder productOrder) {
        productOrder.setStatus(this);
    }

    public String getStatus(){
        return state;
    }

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String,ProductStatus> find = new Finder<>(ProductStatus.class);

}
