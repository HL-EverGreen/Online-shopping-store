package models.payment;


import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CreditCard extends Model {

    @Id
    public Long id;

    // Shipping Info
    String full_name;
    String address;
    String city;
    String state;
    String zip;
    String country;
    String phone;

    // Card Info
    String name;
    String creditCardNumber;

    //TODO: use enums here
    int expiryMonth;
    int expiryYear;
    int cvv;
}
