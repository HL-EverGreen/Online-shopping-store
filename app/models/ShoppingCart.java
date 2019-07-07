package models;

import com.avaje.ebean.Model;
import models.discounts.DiscountApplier;
import models.discounts.LowDiscountStrategy;
import models.product.Product;
import models.product.ProductOrder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingCart extends Model {

    @Id
    public Long id;

    @OneToMany(cascade= CascadeType.ALL)
    List<Product> products = new ArrayList<>();

    @OneToMany(cascade= CascadeType.ALL)
    List<ProductOrder> productOrders = new ArrayList<>();

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Find<Long, ShoppingCart> find = new Find<Long,ShoppingCart>(){};

    public List<Product> getProducts() {
        return products;
    }

    public void updateOrders(List<ProductOrder> updatedProductOrders) {
        productOrders = updatedProductOrders;
    }

    /**
     * Add product to product list
     * @param product
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Remove product from product list
     * @param product
     */
    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<ProductOrder> getProductOrders() {
        return productOrders;
    }

    /**
     * Add product to product orders list
     * @param productOrder
     */
    public void addProductOrder(ProductOrder productOrder) {
        productOrders.add(productOrder);
    }

    /**
     * Remove product from product orders list
     * @param productOrder
     */
    public void removeProductOrder(ProductOrder productOrder) {
        // detach shopping cart from product order
        productOrder.shoppingCart = null;
        productOrder.update();

        // remove product order from cart
        productOrders.remove(productOrder);
    }


}
