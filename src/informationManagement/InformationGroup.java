package informationManagement;

import utilities.Imprend;

import java.util.*;

/**
 * Created by samuel on 30.06.15.
 *
 * A class representing a group of informations, which are all connected to each other.
 * A InformationGroup can contain only Information-Objects, but then, every Information-Object must be a question, where the other are the answer, and the other way around.
 * E.g. when you have 2 words in 2 different languages und you have to learn them in both direction. Then those 2 words are both Information-Objects.
 */
public class InformationGroup {

    private ArrayList<InfoObject> informations;        //All InfoObjects in this InformationGroup. The index of each InfoObject should correspond with it's id.
    //private ArrayList<Question> questions;              //ALl Questions in this InformationGroup
    private Date youngestDate;                          //The youngest date existing in this InformationGroup. (Which Information should be asked for first)
    private String comment;                             //A comment to this InformationGroup, can by anything. The user will write it
    private int id;                                     //The id of the InformationGroup.
    // The ids in the InformationGroup will always be set by the InformationGroup, so every id in it is unique

    public InformationGroup() {
        informations = new ArrayList<>();
        youngestDate = null;
        //fill default values
        comment = "";
        id = -1;
    }

    public int getAmountInformations() {
        return  informations.size();
    }

    public Date getYoungestDate() {
        ArrayList<Date> dates = new ArrayList<>();
        for(int i = 0; i < informations.size(); i++) {
            if(informations.get(i).getType().equals(Imprend.strInfoObjectInfo)) {
                //If it is an Information, not a Question
                Information info = (Information) informations.get(i);
                dates.add(info.getDate());
            }
        }
        Collections.sort(dates);
        youngestDate = dates.get(0);
        return youngestDate;
    }

    public ArrayList<InfoObject> copyAllInformations() {
        return (ArrayList<InfoObject>) informations.clone();
    }

    public void addInformation(InfoObject info) {
        info.setId(getNextId());
        info.setInfoGroupId(id);
        informations.add(info);
    }

    public void removeInformation(int id) {
        //removes an object and looks that all other id, greater than the deleted one are moving up
        if(id >= informations.size()) {
            //id doens't exits
            System.out.println("EROROR: Id doesn't exits. Can't delete InfoObject");
            return;
        }
        informations.remove(id);
        for(int i = id; i < informations.size(); i++) {
            informations.get(i).setId(i);
        }
    }

    public void replaceInfoObject(int id, InfoObject information) {

        informations.set(id, information);
    }

    public InfoObject getInformation(int id) {
        return informations.get(id);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InfoObject getInfoObjectById(int id) {
        //returns the InfoObject with the id given
        return informations.get(id);
    }

    private int getNextId() {
        //returns the next possible id.
        return informations.size();
    }
}

