package models.users;
import models.ShoppingCart;
import models.payment.CreditCard;

import models.users.UserInfo;
import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import java.util.List;

import com.avaje.ebean.Model;

@Inheritance
public class Customer extends UserInfo {

    public List<CreditCard> creditCards;

    ShoppingCart shoppingCart;

    public static Find<String, Customer> find = new Find<String, Customer>(){};

    public void register() {

    }
}
