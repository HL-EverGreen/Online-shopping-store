package models.users;

import models.ShoppingCart;

import models.coupons.Coupons;
import models.product.Product;
import play.data.validation.Constraints;

import com.avaje.ebean.Model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import models.comment.Comment;
import models.notification.NotificationMessage;

import java.util.List;


@Entity
public class UserInfo extends Model{
    @Constraints.MaxLength(30)
    public String name;

    @Id
    @Constraints.Email
    public String emailID;

    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(20)
    private String password;

    @Constraints.Required
    public String type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    public List<Comment> comments;

//    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "user")
//    public List<Coupons> coupons;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "purchaser")
    public List<Product> purchasedProduct = new ArrayList<Product>();

    @OneToOne
    public Long shoppingCartId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "developer")
    public List<Product> ownProducts;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String, UserInfo> find = new Finder<>(UserInfo.class);

    public UserInfo(){}

    public UserInfo(String name, String email, String password, String type) {
        this.name = name;
        this.emailID = email;
        this.password = password;
        this.type = type;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public Boolean isPurchased(String id) {
        for(int i = 0; i < purchasedProduct.size(); i++) {
            if(purchasedProduct.get(i).id.toString().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
