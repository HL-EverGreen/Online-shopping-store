package models.question;

import javax.persistence.*;
import javax.validation.Constraint;

import com.avaje.ebean.Model;

import models.product.Product;
import models.users.Customer;
import models.users.UserInfo;
import play.data.validation.Constraints;

import java.util.List;


@Entity
public class Question extends Model{
    @Id
    public Long id;

    @Constraints.Required
    public String content;

    public String answer;

    @Constraints.Required
    public Long productID;

    public Integer curIndex;

    @ManyToOne
    public QuestionPostCenter poster;

    @ManyToOne
    public QuestionReceiveCenter receiver;

    public Question(){}

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<Long, Question> find = new Finder<>(Question.class);

    public void setReceiverToNext() {
        List<UserInfo> customers = Product.find.byId(productID).purchaser;
        Integer size = customers.size();
        if(size == curIndex + 1) {
            curIndex = -1;
            receiver.removeQuestion(this);
            receiver = null;
        } else {
            curIndex++;
            receiver.removeQuestion(this);
            receiver = QuestionReceiveCenter.find.byId(customers.get(curIndex).emailID);
            receiver.addQuestion(this);
        }
        this.update();
    }
}
