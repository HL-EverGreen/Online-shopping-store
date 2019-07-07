package models;

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
public class Inventory extends Model {
    @Id
    public String userEmail;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventory")
    public List<ProductOrder> productList;

    public Inventory(){}

    public void addProduct(ProductOrder productOrder) {
        this.productList.add(productOrder);
        this.update();
        productOrder.inventory = this;
        productOrder.update();
    }

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String, Inventory> find = new Finder<>(Inventory.class);

}
