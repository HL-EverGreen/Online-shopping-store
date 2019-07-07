package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import models.Inventory;
import models.PurchaseHistory;
import models.ShoppingCart;

import models.notification.NotificationMessage;
import models.product.Product;
import models.UserInterface;
import models.product.ProductOrder;
import models.product.ProductOrderFlyweightFactory;
import models.users.UserInfo;

import models.coupons.Coupons;

import models.comment.ProductComment;
import models.comment.Comment;
import models.comment.CommentManager;
import org.h2.engine.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.List;

public class ProductController extends Controller {

    private FormFactory formFactory;



    @Inject
    public ProductController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * This result directly redirect to application home.
     */
    public Result GO_HOME = Results.redirect(
            routes.ProductController.view(0, "name", "asc", "")
    );

    public Result REFRESH = Results.redirect(
            routes.PurchaseController.createForms()
    );

    /*
    public static class ProductQuant {
        public int quantity;
    }
*/
    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the product to edit
     */
    public Result update(Long id) throws PersistenceException {
        Form<Product> productForm = formFactory.form(Product.class).bindFromRequest();
        if(productForm.hasErrors()) {
            return badRequest(views.html.editProductForm.render(id, productForm));
        }

        Transaction txn = Ebean.beginTransaction();
        try {
            Product savedProduct = Product.find.byId(id);
            if (savedProduct != null) {
                Product newProductData = productForm.get();
                savedProduct.developer = newProductData.developer;
                savedProduct.name = newProductData.name;

                savedProduct.update();
                flash("success", "Product " + productForm.get().name + " has been updated");
                txn.commit();
            }
        } finally {
            txn.end();
        }

        return GO_HOME;
    }

    /**
     * Display the 'edit form' of a existing Product.
     *
     * @param id Id of the computer to edit
     */
    public Result edit(Long id) {
        Form<Product> productForm = formFactory.form(Product.class).fill(
                Product.find.byId(id)
        );
        return ok(
                views.html.editProductForm.render(id, productForm)
        );
    }

    /**
     * Handle default path requests, redirect to products list
     */
    public Result index() {
        return GO_HOME;
    }

    /**
     * Display the 'new product form'.
     */
    public Result create() {
        Form<Product> productForm = formFactory.form(Product.class);
        return ok(
                views.html.createProductForm.render(productForm)
        );
    }

    private UserInfo getUserFromSession() {
        UserInfo user = UserInfo.find.byId(session().get("email"));
        return user;
    }

    /**
     * Display the 'new product order form'.
     */
    public Result createOrder(Long id) {
        // Get quantity
        String quantity = formFactory.form().bindFromRequest().get("quantity");
        String delivery = formFactory.form().bindFromRequest().get("delivery");
        String coupon  = formFactory.form().bindFromRequest().get("coupon");
        Logger.debug("Delivery info: " + delivery);
        Logger.debug("Coupon info: " + coupon);

        // Get the cart of the user
        ShoppingCart cart = ShoppingCart.find.byId(getUserFromSession().shoppingCartId);

        // Get the current product
        /*Product product = Product.find.byId(id);
        product.shoppingCart = cart;
        product.update();*/

        // Get product order from flyweight factory
        ProductOrder productOrder = ProductOrderFlyweightFactory.getProductOrder(id, delivery, quantity, coupon);

        // put the reverse relation of cart in product order
        productOrder.shoppingCart = cart;
        productOrder.save();

        //product.productOrder = productOrder;
        //product.update();

        // Add the product order to cart
        cart.addProductOrder(productOrder);
        cart.update();

        ShoppingCart getCart = ShoppingCart.find.byId(cart.id);
        Product getProduct = Product.find.byId(id);
        ProductOrder getProductOrder = ProductOrder.find.byId(productOrder.id);


        flash("success", "Product details updated in cart");
        return REFRESH;
    }


    /**
     * Add product to cart
     */
    public Result addProductToCart(Long id) {
        // Get the cart of the user
        ShoppingCart cart = ShoppingCart.find.byId(getUserFromSession().shoppingCartId);

        // Get the current product
        Product product = Product.find.byId(id);
        product.shoppingCart = cart;
        product.update();

        // Add the product to cart
        cart.addProduct(product);
        cart.update();

        /*ShoppingCart getCart = ShoppingCart.find.byId(cart.id);
        Product getProduct = Product.find.byId(id);
        ProductOrder getProductOrder = ProductOrder.find.byId(productOrder.id);*/

        flash("success", "Product " + product.name + " has been added to cart");
        return GO_HOME;
    }

    /*
    public Result saveOrder(Long id) {
        Form<ProductOrder> productOrderForm = formFactory.form(ProductOrder.class).bindFromRequest();
        ProductOrder productOrder = productOrderForm.get();
        // Create new order
        //ProductOrder productOrder = new ProductOrder();
        productOrder.product = Product.find.byId(id);
        //productOrder.quantity = productOrder.quantity;

        productOrder.save();
        // Add the cart
        //shoppingCart.addProductOrder(productOrder);
        flash("success", "Product " + id + " has been added to order");
        return GO_HOME;
    }
*/
    /**
     * Display the paginated list of products.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
    public Result view(int page, String sortBy, String order, String filter) {
        Form<ProductOrder> productOrderForm = formFactory.form(ProductOrder.class).bindFromRequest();
        return ok(
                views.html.productlist.render(
                        UserInterface.doSearch(page, 10, sortBy, order, filter),
                        sortBy, order, filter, productOrderForm
                )
        );
    }

    /**
     * Handle the 'new product form' submission
     */
    public Result save() {
        Form<Product> productForm = formFactory.form(Product.class).bindFromRequest();
        if(productForm.hasErrors()) {
            return badRequest(views.html.createProductForm.render(productForm));
        }
        Product product = productForm.get();
        UserInfo developer = UserInfo.find.byId(session().get("email"));
        developer.ownProducts.add(product);
        developer.update();
        product.developer = developer;

        Logger.debug("sessoin " + session().get("email"));
        Logger.debug(product.developer.emailID);

        product.save();
        flash("success", "Product " + productForm.get().name + " has been created");
        return GO_HOME;
    }

    /**
     * Handle product deletion
     */
    public Result delete(Long id) {
        Product.find.ref(id).delete();
        flash("success", "Product has been deleted");
        return GO_HOME;
    }

    /**
     * Post comment on product
     */
    public Result makeComment(Long id) {
        Form<Comment> commentForm = formFactory.form(Comment.class);
        return ok(
                views.html.postComment.render(commentForm, id)
        );
    }

    /**
     * Save comment on product
     */
    public Result saveComment(Long id) {
        Comment comment = formFactory.form(Comment.class).bindFromRequest().get();
        UserInfo user = UserInfo.find.byId(session().get("email"));
        comment.user = user;
        comment.save();
        user.comments.add(comment);
        user.update();
        Product product = Product.find.byId(id);
        CommentManager.attachCommentOnProduct(product, comment);
        flash("success", "Comment to product " + product.name + " has been created");
        return GO_HOME;
    }

    /**
     * View comment on a product
     */
    public Result viewComment(Long id) {
        ProductComment productComment = ProductComment.find.byId(id);
        if(productComment == null) {
            return GO_HOME;
        }
        return ok(
            views.html.viewComment.render(productComment.comments)
        );
    }

    /**
     * View all products developed by certain developer himself.
     */
    public Result viewOwnProduct() {
        System.out.println("ss");
        List<Product> products = UserInfo.find.byId(session().get("email")).ownProducts;
        return ok(
                views.html.viewOwnProduct.render(products)
        );
    }

    /**
     * View all customers purchased certain product
     */
    public Result viewCustomerOfProduct(Long id) {
        Form<NotificationMessage> message = formFactory.form(NotificationMessage.class);
        Product product = Product.find.byId(id);
        List<UserInfo> customers = product.purchaser;
        return ok(
                views.html.viewPurchasedCustomer.render(customers, product, message)
        );
    }

    /**
     * View product inventory of customer
     */
    public Result viewInventory() {
        Inventory inventory = Inventory.find.byId(session().get("email"));
        return ok(
                views.html.viewInventory.render(inventory.productList)
        );
    }

    public Result giveOutCoupons(Long id) {
        Form<Coupons> coupons = formFactory.form(Coupons.class);
        Product product = Product.find.byId(id);
        List<UserInfo> customers = product.purchaser;
        return ok(
                views.html.giveOutCoupons.render(customers, product, coupons)
        );
    }

}
