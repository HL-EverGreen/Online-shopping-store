package models.coupons;

import javax.persistence.*;
import javax.validation.Constraint;

import com.avaje.ebean.Model;

import play.data.validation.Constraints;

import java.util.List;

@Entity
public class Coupons extends Model{
    @Id
    public Long id;

    @Constraints.Required
    public String couponprice;

    @ManyToOne
    public CouponSendCenter sender;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "couponToUser")
    public List<CouponReceiveCenter> receiver;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<Long, Coupons> find = new Finder<>(Coupons.class);
}
