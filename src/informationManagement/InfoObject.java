package informationManagement;

/**
 * Created by samuel on 08.07.15.
 * An Parentobject for the Information and Question-Object so it is possible to return either of them within a method.
 */
public class InfoObject {

    String information;
    int id;
    private int infoGroupId;        //the id of the InformationGroups the Information belongs to

    public InfoObject() {
        //fill default values
        information = "";
        id = -1;
        infoGroupId = -1;
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

    public String getType () {
        String name = getClass().getName();
        String[] array = name.split("\\.");
        return array[array.length-1];
    }

    public int getInfoGroupId() {
        return infoGroupId;
    }

    public void setInfoGroupId(int infoGroupId) {
        infoGroupId = infoGroupId;
    }

}
