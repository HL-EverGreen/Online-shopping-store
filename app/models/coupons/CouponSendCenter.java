package models.coupons;

import javax.persistence.*;
import com.avaje.ebean.Model;

import models.users.UserInfo;

import java.util.List;

@Entity
public class CouponSendCenter extends Model{
    @Id
    public String userEmail;

    public String productID;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    public List<Coupons> coupon;

    public CouponSendCenter(){}

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String, CouponSendCenter> find = new Finder<>(CouponSendCenter.class);
}
