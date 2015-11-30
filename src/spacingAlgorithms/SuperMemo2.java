package spacingAlgorithms;

import java.util.Date;

/**
 * Implementation des SuperMemo2-Algrotihmus von P.A.Wozniak (http://www.supermemo.com/english/ol/sm2.htm) der leicht modifiziert wurde.
 * Es gibt 5 Rückmeldungsmöglichkeiten (Wie gut der Benutzer ein Wort konnte), statt 6 wie im SuperMemo2-Algorithmus: <br>
 * 0 = Gar keine Ahnung <br>
 * 1 = Falsche Antwort <br>
 * 2 = richtige Antwort, aber schwierig zu erinnern <br>
 * 3 = richtige Antwort, gut zu erinnern <br>
 * 4 = richtige, sofortige Antwort <br>
 * Erstellt am 30.06.15
 * @author Samuel Martin
 */
public class SuperMemo2 {

    /**
     * Statische Methode, die das nächste Abfragedatum, berechnet nach dem SuperMemo2-Algorthmus, zurück gibt.
     * Die Methode sollte direkt nach einem Abfragen aufgerufen werden.
     * @param ease  Schwierigkeit der Information
     * @param amountRepetition  Häufigkeit der Wiederholungen
     * @param oldDate  Das letzte Abfragedatum
     * @param date  Das aktuelle Abfragedatum (date-oldDate gibt den letzen Intervall, der benötigt wird)
     * @return nächstes Abfragedatum
     */
    public static Date getNextDate(double ease, int amountRepetition, Date oldDate, Date date) {
        Date nextDate;
        long intervall;
        Date currentDate = new Date(System.currentTimeMillis());

        //get old Intervall in Days:
        //get differenc in msec
        Long oldIntervall = date.getTime() - oldDate.getTime();
        //change unit to days
        oldIntervall /= (1000*60*60*24);


        switch(amountRepetition) {
            case 1:
                intervall = 1;
                break;
            case 2:
                intervall = 6;
                break;
            default:
                intervall =  Math.round(oldIntervall*ease);
        }

        nextDate = new Date(currentDate.getTime() + intervall * (1000*60*60*24));

        return nextDate;
    }

    /**
     * Statische Methode, die eine neue Schwierigkeit, berechnet nach dem SuperMemo2-Algorthmus, zurückgibt.
     * Die Methode sollte direkt nach einem Abfragen aufgerufen werden.
     * @param ease  aktuelle Schwierigkeit
     * @param response  Rückmeldung, die der Benutzer ausgewählt hat
     * @return neue Schwierigkeit
     */
    public static double getNewEase(double ease, int response) {
        if(ease > 1.3) {
            return  1.3;
        }
        return ease+(0.1-(4-response)*(0.08+(4-response)*0.02));
    }

}
