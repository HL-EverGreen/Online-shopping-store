package controllers;

import models.Inventory;
import models.PurchaseHistory;
import models.ShoppingCart;
import models.delivery.Delivery;
import models.payment.CreditCard;
import models.product.Product;
import models.product.ProductOrder;
import models.productStatus.ProductStatus;
import models.purchase.PurchaseOrder;
import models.purchase.PurchaseOrderBuilder;
import models.purchase.PurchaseOrderRequest;
import models.purchase.PurchaseOrderRequestFactory;
import models.users.UserInfo;
import models.coupons.CouponReceiveCenter;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import scala.concurrent.java8.FuturesConvertersImpl;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PurchaseController extends Controller {
    private FormFactory formFactory;

    @Inject
    public PurchaseController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GO_HOME = Results.redirect(
            routes.ProductController.view(0, "name", "asc", "")
    );

    private UserInfo getUserFromSession() {
        UserInfo user = UserInfo.find.byId(session().get("email"));
        return user;
    }

    /**
     * Display the 'forms'.
     */
    public Result createForms() {
        Logger.debug(session().get("email"));
        UserInfo user = UserInfo.find.byId(session().get("email"));
        ShoppingCart cart = ShoppingCart.find.byId(user.shoppingCartId);
        CouponReceiveCenter receiveCenter = CouponReceiveCenter.find.byId(session().get("email"));
        //System.out.println(cart.getProductOrders());
        String gift_card = "";

        Logger.debug("USer:" + user.emailID);

        return ok(
                views.html.purchaseProduct.render(cart.getProducts(), cart.getProductOrders(), receiveCenter, gift_card)
        );
    }

    /**
     * Apply Gift Card.
     */
    public Result selectCountries() {
        String country = formFactory.form().bindFromRequest().get("countries");
        String gift_card;
        UserInfo user = UserInfo.find.byId(session().get("email"));
        ShoppingCart cart = ShoppingCart.find.byId(user.shoppingCartId);
        CouponReceiveCenter receiveCenter = CouponReceiveCenter.find.byId(session().get("email"));
        //System.out.println(cart.getProductOrders());
        if (country.equals("usa")) {
            gift_card = "";
            return ok(
                    views.html.purchaseProduct.render(cart.getProducts(), cart.getProductOrders(), receiveCenter, gift_card)
            );
        }
        gift_card = country;
        return ok(
                views.html.purchaseProduct.render(cart.getProducts(), cart.getProductOrders(), receiveCenter, gift_card)
        );
    }

    /**
     * Display the 'forms'.
     */

    public Result modify(Long id) {

        //Logger.debug(session().get("email"));
        PurchaseOrder purchaseOrder = PurchaseOrder.find.byId(id);
        UserInfo user = UserInfo.find.byId(session().get("email"));
        ShoppingCart cart = ShoppingCart.find.byId(user.shoppingCartId);
        CouponReceiveCenter receiveCenter = CouponReceiveCenter.find.byId(session().get("email"));
        String gift_card = "";

        //Logger.debug("USer:" + user.emailID);

        return ok(
                views.html.purchaseProduct.render(cart.getProducts(), purchaseOrder.getProductOrders(), receiveCenter, gift_card)
        );
    }



    private ProductOrder removeProductOrdersofProduct(Long productId, ShoppingCart cart) {
        List<ProductOrder> updatedProductOrders = new ArrayList<>();

        // Copy to new list to prevent concurrentmodification
        for (ProductOrder order : cart.getProductOrders()) {
            if (order.productId != productId) {
                updatedProductOrders.add(order);
            }
        }

        cart.updateOrders(updatedProductOrders);
        cart.update();
        return null;
    }

    /**
     * Remove the product along with all the product orders corresponding to it.
     */
    public Result removeProductFromCart(Long id) {
        Form<Delivery> deliveryForm = formFactory.form(Delivery.class);
        // Get the cart of the user
        ShoppingCart cart = ShoppingCart.find.byId(getUserFromSession().shoppingCartId);
        CouponReceiveCenter receiveCenter = CouponReceiveCenter.find.byId(session().get("email"));

        // Get the current product and remove from cart
        Product product = Product.find.byId(id);
        product.shoppingCart = null;
        product.update();

        // remove the product from cart
        cart.removeProduct(product);

        // remove the productorders of the product
        removeProductOrdersofProduct(id, cart);

        cart.update();

        /*ShoppingCart getCart = ShoppingCart.find.byId(cart.id);
        Product getProduct = Product.find.byId(id);
        ProductOrder getProductOrder = ProductOrder.find.byId(productOrder.id);*/


        flash("success", "Product has been removed from cart");
        return ok(views.html.purchaseProduct.render(cart.getProducts(), cart.getProductOrders(), receiveCenter, ""));

    }

    /**
     * Remove the product order
     * @param id
     * @return
     */
    public Result removeOrder(Long id) {
        // Get the cart of the user
        ShoppingCart cart = ShoppingCart.find.byId(getUserFromSession().shoppingCartId);
        CouponReceiveCenter receiveCenter = CouponReceiveCenter.find.byId(session().get("email"));

        // Get the current product
        ProductOrder productOrder = ProductOrder.find.byId(id);

        // Add the product order to cart
        cart.removeProductOrder(productOrder);
        cart.update();

        /*ShoppingCart getCart = ShoppingCart.find.byId(cart.id);
        Product getProduct = Product.find.byId(id);
        ProductOrder getProductOrder = ProductOrder.find.byId(productOrder.id);*/

        flash("success", "Product has been removed from cart");
        return ok(views.html.purchaseProduct.render(cart.getProducts(), cart.getProductOrders(), receiveCenter, ""));

    }

    public Result save() {
        // Get the shopping cart
        UserInfo user = UserInfo.find.byId(session().get("email"));
        ShoppingCart cart = ShoppingCart.find.byId(user.shoppingCartId);

        // Build Purchase order
        PurchaseOrderBuilder purchaseOrderBuilder = new PurchaseOrderBuilder(null,
                cart.getProductOrders(), getUserFromSession());
        PurchaseOrder purchaseOrder = purchaseOrderBuilder.build();
        Inventory inventory = Inventory.find.byId(session().get("email"));

        ProductStatus productStatus = ProductStatus.find.byId("ordered");
        if(productStatus == null) {
            productStatus = new ProductStatus();
            productStatus.state = "ordered";
            productStatus.save();
        }

        for(int i = 0; i < cart.getProductOrders().size(); i++) {
            inventory.addProduct(cart.getProductOrders().get(i));
            productStatus.setStatus(cart.getProductOrders().get(i));
        }

        // Is the checkout request special?
        Boolean isSpecial = formFactory.form().bindFromRequest().get("isSpecial") == null ? false : true;
        PurchaseOrderRequestFactory requestFactory = new PurchaseOrderRequestFactory();
        PurchaseOrderRequest request = null;

        if (isSpecial) {
            request = requestFactory.getPurchaseOrderRequest("special", purchaseOrder);
        } else {
            request = requestFactory.getPurchaseOrderRequest("normal", purchaseOrder);
        }

        //TODO: update id here
        return Results.redirect(routes.PlaceOrderController.createForms(purchaseOrder.id));
    }

/*
    public Result purchase(Long id) {
        Form<CreditCard> creditForm = formFactory.form(CreditCard.class).bindFromRequest();
        if(creditForm.hasErrors()) {
            return badRequest(views.html.purchaseProduct.render(creditForm));
        }
        creditForm.get().save();
        return ok(
                views.html.createProductForm.render(creditForm)
        );
    }*/
}
