package InformationManagement;

/**
 * Created by samuel on 30.06.15.
 * Class representing a question. But only a question. It won't be able to act as a answer.
 */
public class Question {
    private String question;
    private int id;

    public Question() {

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setId(int id) {
        this.id = id;
    }
}
