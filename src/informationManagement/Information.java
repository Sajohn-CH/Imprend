package informationManagement;

import java.util.Date;

/**
 * Created by samuel on 30.06.15.
 *
 * Class representing a piece of information. It can act either as a answer to a question or a question itself or something else.
 */
public class Information extends InfoObject{
    private String information;     //The information itself
    private Date date;              //The next date it should be asked last
    private double ease;            //the ease/difficulty of remember the information
    private int amountRepetition;   //How often it has been asked for
    private Date oldDate;
    private int id;                 //the id (identification number)
    private int infoGroupId;        //the id of the InformationGroups the Information belongs to

    public Information() {
        //Set default values.
        date = new Date(0);
        ease = 2.5;
        amountRepetition = 0;
        oldDate = new Date(0);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getEase() {
        return ease;
    }

    public void setEase(double ease) {
        this.ease = ease;
    }

    public int getAmountRepetition() {
        return amountRepetition;
    }

    public void setAmountRepetition(int amountRepetition) {
        this.amountRepetition = amountRepetition;
    }

    public void increaseAmountRepetition() {
        amountRepetition++;
    }

    public Date getOldDate() {
        return oldDate;
    }

    public void setOldDate(Date oldDate) {
        this.oldDate = oldDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getInfoGroupId() {
        return infoGroupId;
    }

    @Override
    public void setInfoGroupId(int infoGroupId) {
        this.infoGroupId = infoGroupId;
    }
}
