package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import models.notification.NotificationMessage;
import models.notification.NotificationReceiveCenter;
import models.notification.NotificationSendCenter;
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
public class MessageController extends Controller{
    private FormFactory formFactory;

    @Inject
    public MessageController(FormFactory formFactory) { this.formFactory = formFactory; }

    /**
     * This result directly redirect to application home.
     */
    public Result GO_HOME = Results.redirect(
            routes.ProductController.view(0, "name", "asc", "")
    );


    /**
     * Send message to all customers who purchased product with id = productID
     *
     * Used mediator pattern
     */
    public Result notifyAllCustomer(Long productID) {
        NotificationMessage message = formFactory.form(NotificationMessage.class).bindFromRequest().get();
        List<UserInfo> customers = Product.find.byId(productID).purchaser;
        NotificationSendCenter sender = NotificationSendCenter.find.byId(session().get("email"));

        message.sender = sender;
        sender.message.add(message);
        sender.update();

        for(int i = 0; i < customers.size(); i++) {
            NotificationReceiveCenter receiver = NotificationReceiveCenter.find.byId(customers.get(i).emailID);
            message.receiver.add(receiver);
        }
        message.save();

        return GO_HOME;
    }

    /**
     * View all notifications from developers
     */
    public Result viewNotifications() {
        NotificationReceiveCenter receiveCenter = NotificationReceiveCenter.find.byId(session().get("email"));
        return ok(
                views.html.notificationCenter.render(receiveCenter)
        );
    }
}
