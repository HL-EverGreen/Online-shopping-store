package models.purchase;

import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;
import com.avaje.ebean.Model;
import models.PurchaseHistory;
import models.discounts.DiscountApplier;
import models.discounts.LowDiscountStrategy;
import models.discounts.MediumDiscountStrategy;
import models.discounts.HighDiscountStrategy;
import models.product.Product;
import models.product.ProductOrder;
import models.delivery.Delivery;
import models.payment.Payment;
import models.productStatus.ProductStatus;
import models.users.Customer;
import models.users.UserInfo;
import models.PurchaseHistory;
import models.PrototypeCapable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for maintaining all the information about a purchase order
 */
@Entity
public class PurchaseOrder extends Model implements PrototypeCapable{

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public UserInfo customer;

    @ManyToOne
    public PurchaseHistory history;

    public String address;

    public String gift_card;

    @OneToMany(cascade=CascadeType.ALL)
    public List<ProductOrder> productOrders;

    DiscountApplier discountApplier;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Find<Long, PurchaseOrder> find = new Find<Long,PurchaseOrder>(){};

    public PurchaseOrder(UserInfo customer, Delivery delivery,
                         List<ProductOrder> productOrders) {
        this.customer = customer;
        //this.delivery = delivery;
        this.productOrders = productOrders;
    }

    void placePurchaseOrder() {

    }

    private Product getProduct(Long id) {
        return Product.find.byId(id);
    }

    /**
     * counts the number of products in the order
     * @return number of products
     */
    private int countProducts() {
        int count = 0;
        for (ProductOrder productOrder : productOrders) {
            count += productOrder.quantity;
        }

        return count;
    }

    public List<ProductOrder> getProductOrders() {
        return productOrders;
    }

    /**
     * calculates the total price of the order
     * @return number of products
     */
    public double calculateOriginPrice() {
        double totalPrice = 0;
        for (ProductOrder productOrder : productOrders) {
            Product product = getProduct(productOrder.productId);
            totalPrice += (product.price * productOrder.quantity);
        }

        return totalPrice;
    }

    public double calculateTotalPrice(){
        double totalPrice = 0;
        for (ProductOrder productOrder : productOrders) {
            Product product = getProduct(productOrder.productId);
            double singleprice = coupon_discount(productOrder.coupon, product.price);
            totalPrice += (singleprice * productOrder.quantity);
        }

        return totalPrice;
    }

    public String determineDiscount() {
        int count = countProducts();
        discountApplier = null;
        String discountString = "No discount applied";

        // Add More rules here
        if (count >= 2 && count <= 4) {
            // Get the discount applier with low discount strategy
            discountApplier = new DiscountApplier(new LowDiscountStrategy());
            discountString = "Discount of 20% applied because you bought " + count + " products in total";
        } else if (count >= 5 && count <= 9) {
            // Get the discount applier with medium discount strategy
            discountApplier = new DiscountApplier(new MediumDiscountStrategy());
            discountString = "Discount of 40% applied because you bought " + count + " products in total";
        }

        return discountString;
    }

    /**
     * checkout method applies the discounts dynamically and
     * returns the total updated price
     * @return discounted price
     */
    public Double checkout() {
        int count = countProducts();
        double totalPrice = calculateTotalPrice();
        NormalFee normalFee = new NormalFee(totalPrice);
        discountApplier = null;

        // Add More rules here
        if (count >= 2 && count <= 4) {
            // Get the discount applier with low discount strategy
            discountApplier = new DiscountApplier(new LowDiscountStrategy());
        } else if (count >= 5 && count <= 9) {
            // Get the discount applier with medium discount strategy
            discountApplier = new DiscountApplier(new MediumDiscountStrategy());
        }

        PurchaseFeeDiscountDecorator discountDecorator = new PurchaseFeeDiscountDecorator(normalFee, discountApplier);
        return discountDecorator.getFee();
    }

    private double coupon_discount(String coupon, double originalprice) {
        NormalFee normalFee = new NormalFee(originalprice);
        discountApplier = null;

        // Add More rules here
        if (coupon.equals("20%")) {
            // Get the discount applier with low discount strategy
            discountApplier = new DiscountApplier(new LowDiscountStrategy());
        } else if (coupon.equals("40")) {
            // Get the discount applier with medium discount strategy
            discountApplier = new DiscountApplier(new MediumDiscountStrategy());
        } else if (coupon.equals("60%")){
            discountApplier = new DiscountApplier(new HighDiscountStrategy());
        }

        //TODO: Apply the discount announced by developer here

        PurchaseFeeDiscountDecorator discountDecorator = new PurchaseFeeDiscountDecorator(normalFee, discountApplier);
        return discountDecorator.getFee();
    }

    @Override
    public PurchaseOrder clone() throws CloneNotSupportedException {
        System.out.println("Cloning Purchase Order object..");
        return (PurchaseOrder) super.clone();
    }

    public void addProductOrder(ProductOrder productOrder) {
        this.productOrders.add(productOrder);
        this.update();
        productOrder.purchaseOrder = this;
        productOrder.update();
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
