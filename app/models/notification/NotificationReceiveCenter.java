package models.notification;

import javax.persistence.*;
import com.avaje.ebean.Model;

import models.users.UserInfo;

import java.util.List;

@Entity
public class NotificationReceiveCenter extends Model {
    @Id
    public String userEmail;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<NotificationMessage> messageToUser;

    public NotificationReceiveCenter(){}
    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String, NotificationReceiveCenter> find = new Finder<>(NotificationReceiveCenter.class);
}
