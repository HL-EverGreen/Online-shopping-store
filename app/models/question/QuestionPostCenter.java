package models.question;

import javax.persistence.*;
import javax.validation.Constraint;

import com.avaje.ebean.Model;

import play.data.validation.Constraints;

import java.util.List;

@Entity
public class QuestionPostCenter extends Model {
    @Id
    public String userEmail;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "poster")
    public List<Question> questions;

    public QuestionPostCenter(){}

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String, QuestionPostCenter> find = new Finder<>(QuestionPostCenter.class);

    public void addQuestion(Question question) {
        questions.add(question);
        this.update();
    }

}
