package models.notification;

import javax.persistence.*;
import javax.validation.Constraint;

import com.avaje.ebean.Model;

import play.data.validation.Constraints;

import java.util.List;

@Entity
public class NotificationMessage extends Model{
    @Id
    public Long id;

    @Constraints.Required
    public String content;

    @ManyToOne
    public NotificationSendCenter sender;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "messageToUser")
    public List<NotificationReceiveCenter> receiver;

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<Long, NotificationMessage> find = new Finder<>(NotificationMessage.class);

}
