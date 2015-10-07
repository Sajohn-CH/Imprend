package informationManagement;

/**
 * Created by samuel on 30.06.15.
 * Class representing a question. But only a question. It won't be able to act as a answer.
 */
public class Question extends InfoObject{
    //private String question;
    //private int id;
    //private int infoGroupId;        //the id of the InformationGroups the Information belongs to

    public Question() {
        //fill default values
        information = "";
        id = -1;
        infoGroupId = -1;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return information;
    }

    @Override
    public void setInfoGroupId(int infoGroupId) {
        this.infoGroupId = infoGroupId;
    }

    @Override
    public int getInfoGroupId() {
        return infoGroupId;
    }
}
