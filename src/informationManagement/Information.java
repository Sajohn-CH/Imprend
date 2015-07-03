package informationManagement;

import java.util.Date;

/**
 * Created by samuel on 30.06.15.
 *
 * Class representing a piece of information. It can act either as a answer to a question or a question itself or something else
 */
public class Information {
    private String information;
    private Date date;
    private double ease;
    private int amountRepetition;
    private Date oldDate;
    //private int id;

    public Information() {

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
}
