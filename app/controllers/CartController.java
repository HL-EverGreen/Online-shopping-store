package controllers;

import models.product.Product;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import models.product.ProductOrder;
import javax.inject.Inject;


public class CartController extends Controller {
    private FormFactory formFactory;
    @Inject
    public CartController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * This result directly redirect to application home.
     */
    public Result GO_HOME = Results.redirect(
            routes.ProductController.view(0, "name", "asc", "")
    );

    /**
     * Handle the 'product order form' submission
     *
     * @param id Id of the product to edit
     * @param quantity Quantity of the product in the order
     */
    /**
     * Display the 'new product order form'.
     */

    public Result create() {
        Form<ProductOrder> productOrderForm = formFactory.form(ProductOrder.class);
        return ok(
                views.html.shoppingCart.render(productOrderForm)
        );
    }


    /**
     * Handle the 'new product order form' submission
     */

    public Result save(Long id) {
        Form<ProductOrder> productOrderForm = formFactory.form(ProductOrder.class).bindFromRequest();
        if(productOrderForm.hasErrors()) {
            return badRequest(views.html.shoppingCart.render(productOrderForm));
        }
        ProductOrder productOrder = productOrderForm.get();
        productOrder.productId = id;
        //productOrder.quantity = quantity;

        productOrder.save();
        flash("success", "Order has been created");
        return GO_HOME;
    }


}
