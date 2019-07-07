package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import models.Inventory;
import models.PurchaseHistory;
import models.question.QuestionPostCenter;
import models.question.QuestionReceiveCenter;
import models.coupons.CouponReceiveCenter;
import models.coupons.CouponSendCenter;
import models.users.Customer;
import models.users.Developer;
import models.notification.NotificationSendCenter;
import models.notification.NotificationReceiveCenter;
import models.ShoppingCart;
import models.users.UserInfo;
import org.h2.engine.User;
import play.Logger;
import views.html.loginPage;
import views.html.loginPage_CN;
import views.html.registerPage;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;

@Singleton
public class UserController extends Controller{
    private FormFactory formFactory;

    @Inject
    public UserController(FormFactory formFactory) { this.formFactory = formFactory; }

    // TODO: add complete validation in form.
//    public String validate() {
//        if (User.authenticate(email, password) == null) {
//            return "Invalid user or password";
//        }
//        return null;
//    }


    public Result login() {
        Form<UserInfo> loginForm = formFactory.form(UserInfo.class);
        return ok(loginPage.render(loginForm));
    }

    public Result login_CN() {
        Form<UserInfo> loginForm = formFactory.form(UserInfo.class);
        return ok(loginPage_CN.render(loginForm));
    }


    public Result doLogin() {
        final Form<UserInfo> filledForm = formFactory.form(UserInfo.class).bindFromRequest();

        if(filledForm.hasErrors()) {
            return badRequest(loginPage.render(filledForm));
        }

        UserInfo loginUser = filledForm.get();
        UserInfo recordedUser = UserInfo.find.byId(loginUser.emailID);

        if(recordedUser == null || !recordedUser.getPassword().equals(loginUser.getPassword())) {
            return badRequest(loginPage.render(filledForm));
        }
        flash("success", "Customer " + recordedUser.name + " has been logged in");

        return writeSession(loginUser);
    }


    public Result register() {
        Form<UserInfo> registerForm = formFactory.form(UserInfo.class);
        return ok(registerPage.render(registerForm));
    }


    public Result doRegister() {
        final Form<UserInfo> filledForm = formFactory.form(UserInfo.class).bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(registerPage.render(filledForm));
        }
        UserInfo newUser = filledForm.get();

        if(UserInfo.find.byId(newUser.emailID) != null) {
            return badRequest(registerPage.render(filledForm));
        }

        ShoppingCart cart = new ShoppingCart();
        cart.save();

        newUser.shoppingCartId = cart.id;
        newUser.save();

        UserInfo u = UserInfo.find.byId(newUser.emailID);
        Logger.debug(u.emailID);

        if(newUser.type.equals("Developer")) {
            NotificationSendCenter sendCenter = new NotificationSendCenter();
            sendCenter.userEmail = newUser.emailID;
            sendCenter.save();
            CouponSendCenter couponSend = new CouponSendCenter();
            couponSend.userEmail = newUser.emailID;
            couponSend.save();
        } else if(newUser.type.equals("Customer")) {
            NotificationReceiveCenter receiveCenter = new NotificationReceiveCenter();
            receiveCenter.userEmail = newUser.emailID;
            receiveCenter.save();

            QuestionPostCenter postCenter = new QuestionPostCenter();
            postCenter.userEmail = newUser.emailID;
            postCenter.save();

            QuestionReceiveCenter questionReceiveCenter = new QuestionReceiveCenter();
            questionReceiveCenter.userEmail = newUser.emailID;
            questionReceiveCenter.save();

            Inventory inventory = new Inventory();
            inventory.userEmail = newUser.emailID;
            inventory.save();

            PurchaseHistory history = new PurchaseHistory();
            history.userEmail = newUser.emailID;
            history.save();

            CouponReceiveCenter couponReceive = new CouponReceiveCenter();
            couponReceive.userEmail = newUser.emailID;
            couponReceive.save();

        }

        flash("success", newUser.type + " " + newUser.emailID + " has been registered");

        return writeSession(newUser);
    }


    public Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return Results.redirect(
                routes.ProductController.view(0, "name", "asc", ""));
    }

    private Result writeSession(UserInfo userinfo) {
        // Modify session for logged in user
        session().clear();
        session("email", userinfo.emailID);
        session("type", userinfo.type);

        if(userinfo.name != null) {
            session("username", userinfo.name);
        } else {
            UserInfo oldUser = UserInfo.find.byId(userinfo.emailID);
            if(oldUser.name != null) {
                session("username", oldUser.name);
            }
        }
        return Results.redirect(
                routes.ProductController.view(0, "name", "asc", ""));
    }

}