package models.question;

import javax.persistence.*;
import javax.validation.Constraint;

import com.avaje.ebean.Model;

import play.data.validation.Constraints;

import java.util.List;

@Entity
public class QuestionReceiveCenter extends  Model {
    @Id
    public String userEmail;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "receiver")
    public List<Question> questions;

    public QuestionReceiveCenter(){}

    /**
     * Generic query helper for entity Product with id Long
     */
    public static Finder<String, QuestionReceiveCenter> find = new Finder<>(QuestionReceiveCenter.class);

    public void removeQuestion(Question question) {
        Long id = question.id;
        for(int i = 0; i < questions.size(); i++) {
            if(questions.get(i).id.toString().equals(id.toString())) {
                questions.remove(i);
                System.out.println("Succ delete question " + id.toString() + " in receiver");
                break;
            }
        }
        this.update();
    }

    public void addQuestion(Question question) {
        questions.add(question);
        this.update();
    }
}
