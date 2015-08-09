package informationManagement;

/**
 * Created by samuel on 30.06.15.
 * Class representing a question. But only a question. It won't be able to act as a answer.
 */
public class Question extends InfoObject{
    private String question;
    private int id;
    private int infoGroupId;        //the id of the InformationGroups the Information belongs to

    public Question() {
        //fill default values
        question = "";
        id = -1;
        infoGroupId = -1;
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

    public String getQuestion() {
        return question;
    }

    @Override
    public void setInfoGroupId(int infoGroupId) {
        infoGroupId = infoGroupId;
    }

    @Override
    public int getInfoGroupId() {
        return infoGroupId;
    }
}
