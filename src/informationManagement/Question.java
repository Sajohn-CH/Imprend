package informationManagement;

/**
 * Created by samuel on 30.06.15.
 * Class representing a question. But only a question. It won't be able to act as a answer.
 */
public class Question extends InfoObject{
    private String question;
    private int id;

    public Question() {

    }

    public String getInformation() {
        return question;
    }

    public void setInformation(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
