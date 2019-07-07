package models.notification;

import javax.persistence.*;
import com.avaje.ebean.Model;

import models.users.UserInfo;

import java.util.List;

@Entity
public class NotificationSendCenter extends Model {
    @Id
    public String userEmail;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    public List<NotificationMessage> message;

    public NotificationSendCenter(){}

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String, NotificationSendCenter> find = new Finder<>(NotificationSendCenter.class);

}
