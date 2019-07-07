package models.comment;

import com.avaje.ebean.Model;

import org.h2.engine.User;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Constraint;

import models.users.UserInfo;

@Entity
public class Comment extends Model{
    @Id
    public Long id;

    @Constraints.Required
    public String content;

    @ManyToOne
    public UserInfo user;

    @ManyToOne
    public ProductComment productcomment;

    public Comment(){}
    /**
     * Generic query helper for entity Product with id Long
     */
    public static Model.Find<Long, Comment> find = new Find<Long, Comment>(){};
}
