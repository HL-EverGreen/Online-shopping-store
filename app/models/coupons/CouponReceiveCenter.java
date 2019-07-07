package models.coupons;

import javax.persistence.*;
import com.avaje.ebean.Model;

import models.users.UserInfo;

import java.util.List;

@Entity
public class CouponReceiveCenter extends Model {
    @Id
    public String userEmail;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Coupons> couponToUser;

    public CouponReceiveCenter(){}
    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String, CouponReceiveCenter> find = new Finder<>(CouponReceiveCenter.class);
}
