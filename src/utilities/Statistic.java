package utilities;

import java.util.HashMap;

/**
 * Merkt sich wieviele Karten an welchem Tag gelernt wurden. Das Datum wird als String gespeichert, das Format gibt {@link Imprend#dfDate} vor.
 *
 * @author Samuel Martin
 * Erstellt am 02.03.16.
 */
public class Statistic {

    private HashMap<String, Integer> learned;

    public Statistic() {
        learned = new HashMap<>();
    }

    /**
     * Erhöht die Anzahl der gelernten Karten von Heute um eins.
     */
    public void addCardLearnedToday() {
        String dateToday = Imprend.dfDate.format(System.currentTimeMillis());
        learned.put(dateToday, getLearned(dateToday)+1);
    }

    /**
     * Gibt zurück wie viele Karten Heute gelernt wurden,
     * @return Karten gelernt
     */
    public int getLearnedToday() {
        //return cardsLearnedToday;
        return getLearned(Imprend.dfDate.format(System.currentTimeMillis()));
    }

    /**
     * Gibt zurück wie viele Karten an einem bestimmten Tag gelernt wurden.
     * @param date Gewünschter Tag
     * @return gelernt am gewünschten Tag
     */
    public int getLearned(String date) {
        if(learned.get(date) != null) {
            return learned.get(date);
        } else {
            return 0;
        }
    }


    /**
     * Gibt eine HashMap mit allen Daten, wann wie viel gelernt wurde zurück.
     * @return Statistik wie viel gelernt wurde
     */
    public HashMap<String, Integer> getLearned() {
        return learned;
    }

    /**
     * Setzt ein bestimmtes Datum bezügliche der Menge, wie viel gelernt wurde.
     * @param date Datum
     * @param amount Menge gelernt
     */
    public void setLearned(String date, int amount) {
        learned.put(date, amount);
    }
}
