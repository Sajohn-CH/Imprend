package informationManagement;

import java.util.Date;

/**
 * Kindklasse von InfoObject. Stellt eine Information dar (ist meistens ein Wort).
 * Es besitzt, neben den Attributen, die es vom InfoObject erbt folgende Attribute: <br>
 * -Date Date: Das nächste Abfragedatum (Wann die Information zum nächsten Mal abgefragt wird). <br>
 * -double ease: Die Schwierigkeit der Information. Wie schwer es ist sich daran zu erinnern. <br>
 * -int amountRepetition: Die Anzahl an Abfragungen / Wiederholnungen, die die Information hinter sich hat <br>
 * -Date oldDate: Das letzte Abfragedatum <br>
 * Erstellt am 30.06.15
 * @author Samuel Martin
 * {@inheritDoc}
 */
public class Information extends InfoObject{

    private Date date;              //The next date it should be asked last
    private double ease;            //the ease/difficulty of remembering the information
    private int amountRepetition;   //How often it has been asked for
    private Date oldDate;           //The last date, the Information should have been asked. Used to calculate the intervall between the last two dates, to aks.

    /**
     * Kinstruktor. Initialisiert alle Variablen mit Standardwerten
     */
    public Information() {
        //Set default values.
        super();
        date = new Date(0);
        ease = 2.5;
        amountRepetition = 0;
        oldDate = new Date(0);
    }

    /**
     * Liefert das nächstes Abfragedatum
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setzt das nächste Abfragedatum
     * @param date  nächstes Abfragedatum
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Liefert die Schierigkeit der Information
     * @return ease
     */
    public double getEase() {
        return ease;
    }

    /**
     * Setzt die Schwierigkeit der Information
     * @param ease Schwierigkeit
     */
    public void setEase(double ease) {
        this.ease = ease;
    }

    /**
     * Liefert die Anzahl der Abfragungen
     * @return amountRepetition
     */
    public int getAmountRepetition() {
        return amountRepetition;
    }

    /**
     * Setzt die Anzahl der Abfragungen auf eine bestimmte Zahl (z.B: zum Zurücksetzten)
     * @param amountRepetition Amzahl Abfragungen
     */
    public void setAmountRepetition(int amountRepetition) {
        this.amountRepetition = amountRepetition;
    }

    /**
     * Erhoeht die Anzahl der Abfragunen um eine. Wird bei jedem Abfragen einmal aufgerufen.
     */
    public void increaseAmountRepetition() {
        amountRepetition++;
    }

    /**
     * Gibt das letzte (alte) Abfragedatum zurueck
     * @return oldDate
     */
    public Date getOldDate() {
        return oldDate;
    }


    /**
     * Setzt das letzte Abfragedatum
     * @param oldDate letztes Abfragedatum
     */
    public void setOldDate(Date oldDate) {
        this.oldDate = oldDate;
    }

}
