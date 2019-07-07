package models.delivery;

import com.avaje.ebean.Model;
import models.purchase.PurchaseOrder;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Constraint;
import java.util.Arrays;
import java.util.List;


@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "user_type")
public class Delivery extends Model {

    //TODO: make id something here
    @Id
    public Long id;

    //private static Long count = 0L;

    @Constraints.Required
    public String deliveryMethod;

    public Delivery(String deliveryMethod) {
        //this.id = count ++;
        this.deliveryMethod = deliveryMethod;
    }

    public static Model.Find<Long, Delivery> find = new Find<Long,Delivery>(){};


    //private static List<String> listDeliveryMethods
      //      = Arrays.asList("dvd", "online", "donwload");

    public void deliver() {

    }
}

