package controllers;

import models.product.Product;
import models.question.Question;
import models.question.QuestionPostCenter;
import models.question.QuestionReceiveCenter;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class QuestionController extends Controller {
    private FormFactory formFactory;

    @Inject
    public QuestionController(FormFactory formFactory) { this.formFactory = formFactory; }

    /**
     * This result directly redirect to application home.
     */
    public Result GO_HOME = Results.redirect(
            routes.ProductController.view(0, "name", "asc", "")
    );


    /**
     * Render page to allow customers post question about specific product
     */
    public Result postQuestion() {
        Form<Question> postQuestionForm = formFactory.form(Question.class);
        return ok(
                views.html.postQuestion.render(postQuestionForm, "default")
        );
    }


    /**
     * Save question posted by customer
     */
    public Result saveQuestion() {
        Form<Question> questionForm = formFactory.form(Question.class).bindFromRequest();
        if(questionForm.hasErrors()) {
            return badRequest(views.html.postQuestion.render(questionForm, "Form has error!"));
        }

        Question question = questionForm.get();

        Product product = Product.find.byId(question.productID);

        if(product.purchaser.isEmpty()) {
            return ok(
                    views.html.postQuestion.render(questionForm, "Sorry... But this product hasn't been bought by anyone yet.")
            );
        }

        question.curIndex = 0;
        question.answer = "";
        QuestionPostCenter poster = QuestionPostCenter.find.byId(session().get("email"));
        poster.addQuestion(question);
        question.poster = poster;

        QuestionReceiveCenter receiver = QuestionReceiveCenter.find.byId(product.purchaser.get(0).emailID);
        receiver.addQuestion(question);
        question.receiver = receiver;

        question.save();

        return GO_HOME;
        // Check if product has some customers who bought it
    }


    /**
     * Render page to allow customers post question about specific product
     */
    public Result viewQuestion() {
        QuestionReceiveCenter receiveCenter = QuestionReceiveCenter.find.byId(session().get("email"));
        return ok(
                views.html.viewQuestion.render(receiveCenter)
        );
    }


    /**
     * Render page to allow customers view all questions about specific product they have posted
     */
    public Result viewPostedQuestion() {
        QuestionPostCenter postCenter = QuestionPostCenter.find.byId(session().get("email"));

        return ok(
                views.html.viewPostedQuestion.render(postCenter)
        );
    }

    public Result answerQuestion(Long id) {
        Question question = Question.find.byId(id);

        Form<Question> answerQuestionForm = formFactory.form(Question.class).fill(question);

        return ok(
                views.html.answerQuestion.render(answerQuestionForm, question, "default")
        );
    }

    public Result saveAnswerToQuestion(Long id) {
        Question question = formFactory.form(Question.class).bindFromRequest().get();

        if(question.answer == "") {
            Form<Question> answerQuestionForm = formFactory.form(Question.class).fill(question);
            return ok(
                    views.html.answerQuestion.render(answerQuestionForm, question, "You should enter your answer before publishing!")
            );
        }

        Question origin_question = Question.find.byId(id);

        origin_question.answer = question.answer;

        origin_question.receiver.removeQuestion(origin_question);
        origin_question.receiver = null;
        origin_question.curIndex = 0;
        origin_question.update();

        return GO_HOME;
    }

    public Result passQuestion(Long id) {
        Question question = Question.find.byId(id);

        Product product = Product.find.byId(question.productID);

        question.receiver.removeQuestion(question);

        if(product.purchaser.size() == question.curIndex + 1) {
            question.curIndex = -1;
            question.receiver = null;
        } else {
            question.curIndex++;
            QuestionReceiveCenter receiver = QuestionReceiveCenter.find.byId(product.purchaser.get(question.curIndex).emailID);
            receiver.addQuestion(question);
            question.receiver = receiver;
        }
        question.update();

        QuestionReceiveCenter receiveCenter = QuestionReceiveCenter.find.byId(session().get("email"));
        return ok(
                views.html.viewQuestion.render(receiveCenter)
        );
    }

}
