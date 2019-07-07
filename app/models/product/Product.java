package models.product;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import models.Inventory;
import models.ShoppingCart;
import models.users.UserInfo;
import play.data.validation.Constraints;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
public class Product extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public String name;

    //@Constraints.Required
    public Double price;

    @ManyToOne
    public UserInfo developer;

    @ManyToOne
    public ShoppingCart shoppingCart;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<UserInfo> purchaser = new ArrayList<UserInfo>();

    //@OneToOne
    //public ProductOrder productOrder;

    // Product can be provided with a discount by the developer
    //public DiscountStrategy discountStrategy;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Find<Long,Product> find = new Find<Long,Product>(){};

    /**
     * Return a paged list of products
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Product property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static PagedList<Product> page(int page, int pageSize, String sortBy, String order, String filter) {
        return
                find.where()
                        .ilike("name", "%" + filter + "%")
                        .orderBy(sortBy + " " + order)
                        .fetch("developer")
                        .findPagedList(page, pageSize);
    }

    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Product product: Product.find.orderBy("name").findList()) {
            options.put(product.id.toString(), product.name);
        }
        return options;
    }
}
