package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import models.coupons.CouponReceiveCenter;
import models.coupons.CouponSendCenter;
import models.coupons.Coupons;
import models.product.Product;
import models.users.UserInfo;
import org.h2.engine.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import java.util.List;

@Singleton
public class CouponController extends Controller{
    private FormFactory formFactory;

    @Inject
    public CouponController(FormFactory formFactory) { this.formFactory = formFactory; }

    /**
     * This result directly redirect to application home.
     */
    public Result GO_HOME = Results.redirect(
            routes.ProductController.view(0, "name", "asc", "")
    );


    public Result sendCoupon() {
        Form<Coupons> sendCouponForm = formFactory.form(Coupons.class);
        return ok(
                views.html.sendCoupons.render(sendCouponForm)
        );
    }

    /**
     * Send coupon to all customers who purchased product with id = productID
     */
    public Result notifyAllCustomer(Long productID) {
        Coupons coupon = formFactory.form(Coupons.class).bindFromRequest().get();
        List<UserInfo> customers = Product.find.byId(productID).purchaser;
        CouponSendCenter sender = CouponSendCenter.find.byId(session().get("email"));

        coupon.sender = sender;
        sender.coupon.add(coupon);
        sender.productID = Product.find.byId(productID).name;
        sender.update();

        for(int i = 0; i < customers.size(); i++) {
            CouponReceiveCenter receiver = CouponReceiveCenter.find.byId(customers.get(i).emailID);
            coupon.receiver.add(receiver);
        }
        coupon.save();

        return GO_HOME;
    }

    /**
     * View all notifications from developers
     */
    public Result viewCoupons() {
        CouponReceiveCenter receiveCenter = CouponReceiveCenter.find.byId(session().get("email"));
        return ok(
                views.html.allcoupons.render(receiveCenter)
        );
    }
}
