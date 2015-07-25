package informationManagement;

/**
 * Created by samuel on 08.07.15.
 * An Parentobject for the Information and Question-Object so it is possible to return either of them within a method.
 */
public class InfoObject {

    String information;
    int id;

    public InfoObject() {
    }

    public String getInformation() {
        return "";
    }

    public void setInformation(String info) {
        this.information = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
