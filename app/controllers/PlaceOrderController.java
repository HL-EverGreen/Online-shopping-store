package controllers;

import models.Inventory;
import models.PrototypeCapable;
import models.PurchaseHistory;
import models.delivery.Delivery;
import models.payment.CreditCard;
import models.product.Product;
import models.productStatus.ProductStatus;
import models.ShoppingCart;
import models.purchase.OrderCache;
import models.purchase.PurchaseOrder;
import models.users.UserInfo;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceOrderController extends Controller {

    private FormFactory formFactory;

    @Inject
    public PlaceOrderController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GO_HOME = Results.redirect(
            routes.ProductController.view(0, "name", "asc", "")
    );

    /**
     * Display the 'forms'.
     */
    public Result createForms(Long purchaseOrderId) {
        Form<CreditCard> creditCardForm = formFactory.form(CreditCard.class);

        PurchaseOrder purchaseOrder = PurchaseOrder.find.byId(purchaseOrderId);
        flash("success", purchaseOrder.determineDiscount());

        // get the product orders list
        Logger.debug(session().get("email"));
        UserInfo user = UserInfo.find.byId(session().get("email"));
        ShoppingCart cart = ShoppingCart.find.byId(user.shoppingCartId);

        // TODO: pass order to html and pass it back!!
        return ok(
                views.html.placeorder.render(creditCardForm, purchaseOrder.calculateOriginPrice(), purchaseOrder.calculateTotalPrice(), purchaseOrder.checkout(), purchaseOrderId, cart.getProductOrders())
        );
    }

    /**
     * Display the 'forms'.
     */
    public Result createVisaForm(Long purchaseOrderId) {
        Form<CreditCard> creditCardForm = formFactory.form(CreditCard.class);

        PurchaseOrder purchaseOrder = PurchaseOrder.find.byId(purchaseOrderId);
        flash("success", purchaseOrder.determineDiscount());

        // get the product orders list
        Logger.debug(session().get("email"));
        UserInfo user = UserInfo.find.byId(session().get("email"));
        ShoppingCart cart = ShoppingCart.find.byId(user.shoppingCartId);

        // TODO: pass order to html and pass it back!!
        return ok(
                views.html.visapayment.render(creditCardForm, purchaseOrder.calculateOriginPrice(), purchaseOrder.calculateTotalPrice(), purchaseOrder.checkout(), purchaseOrderId, cart.getProductOrders())
        );
    }


    public Result save(Long purchaseOrderID) {
        Form<CreditCard> creditCardForm = formFactory.form(CreditCard.class).bindFromRequest();
        OrderCache orderCache = new OrderCache();

        if(creditCardForm.hasErrors()) {
            return badRequest(views.html.placeorder.render(creditCardForm, 0.0, 0.0, 0.0, purchaseOrderID, null));
        }

        creditCardForm.get().save();

        PurchaseOrder purchaseOrder = PurchaseOrder.find.byId(purchaseOrderID);
        purchaseOrder.setAddress(formFactory.form().bindFromRequest().get("address"));
        purchaseOrder.update();
        PurchaseHistory history = PurchaseHistory.find.byId(session().get("email"));
        //System.out.println(purchaseOrder.productOrders);
        history.addOrder(purchaseOrder);
        history.update();

        UserInfo purchaser = UserInfo.find.byId(session().get("email"));
        ProductStatus productStatus = ProductStatus.find.byId("paid");
        if(productStatus == null) {
            productStatus = new ProductStatus();
            productStatus.state = "paid";
            productStatus.save();
        }

        for(int i = 0; i < purchaseOrder.productOrders.size(); i++) {
            productStatus.setStatus(purchaseOrder.productOrders.get(i));
            Product product = Product.find.byId(purchaseOrder.productOrders.get(i).productId);
            //product.purchaser.add(purchaser);
            //product.update();
            if(purchaser.isPurchased(product.id.toString()) == false) {
                purchaser.purchasedProduct.add(product);
            }
        }

        purchaser.update();

        flash("success", "Product has been purchased");
        return GO_HOME;
    }

    /**
     * View purchase history of customer
     */
    public Result viewHistory() {
        PurchaseHistory purchaseHistory = PurchaseHistory.find.byId(session().get("email"));
        return ok(
                views.html.viewHistory.render(purchaseHistory.purchaseOrderList)
        );
    }

}
