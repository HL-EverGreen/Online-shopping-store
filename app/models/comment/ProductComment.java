package models.comment;

import com.avaje.ebean.Model;

import com.avaje.ebean.annotation.DbJson;
import models.users.UserInfo;
import org.h2.engine.User;
import play.data.validation.Constraints;

import javax.persistence.*;

import java.util.List;

import models.comment.Comment;
import models.product.Product;

@Entity
public class ProductComment extends Model{
    @Id
    public Long productID;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productcomment")
    public List<Comment> comments;

    public ProductComment(Product product, Comment comment) {
        this.comments.add(comment);
        this.productID = product.id;
    }
//
//    public ProductComment(ProductComment productComment) {
//        this.productID = productComment.productID;
//        for(int i = 0; i < productComment.comments.size(); i++) {
//            this.comments.add(productComment.comments.get(i));
//        }
//    }
//
//    public ProductComment addComment(Comment comment) {
//        if(this.comments == null) {
//            System.out.println("creating new arraylist..");
//        }
//        this.comments.add(comment);
//        return this;
//    }
    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<Long, ProductComment> find = new Finder<>(ProductComment.class);
}