package questionMethods;

import java.util.ArrayList;

/**
 * Abstrakte Methode, welche alle Methoden vorgibt, die eine Abfragemethode überschrieben haben sollte, damit sie mit dem bestehenden Code zusammenarbeitet.
 * Die Klasse ist dafür zuständig der graphischen Oberfläche nacheinander alle Karten zu geben und das nächste Abfragedatum zu berechnen
 * (mit einer Klasse aus dem Package {@link spacingAlgorithms}). <br>
 * Erstellt am 30.06.15.
 * @author Samuel Martin
 */
public abstract class QuestionMethod {

    /**
     * Liefert die nächste Karte, die gelernt werden muss. Aufbau der ArrayList: Frage, Antwort, Synonyme, Synonyme zur Antwort. Wenn es keine Synonyme (zur Antwort) gibt, ist es nur ein leerer String.
     * @return ArrayList<String> mit der naechsten Karten
     */
    public ArrayList<String> getNextCard() {
        //Returns the information of the informations/questions of the next Cards
        return null;
    }

    /**
     * Wird von der grapischen Oberfläche aufgerufen, um mitzuteilen wie gut der Benutzer die Information erinnern konnte.
     * @param response  Wie gut der Benutzer sich erinnerte.
     */
    public void setResponse(int response) {
        //Is being called by the Panel, "asking the questions" to say how good the user remembered the informations
    }

    /**
     * Gibt eine Vorhersage für das nächste Abfragedatum (bzw. den Intervall) mit der gegebenen Rückmeldung (wie gut das Wort erinnert wurde)
     * @param response  Rückmeldung
     * @return Vorhergesagte Intervall (Zeit bis zum nächsten Abfragedatum)
     */
    public long getPredictedInterval(int response) {
        //returns the predicted interval (when the card will be asked the next time) for the given answer
        return 0;
    }

    /**
     * Wird von der Grapischen Oberfläche aufgerufen, wenn der Stapel fertig gelernt wurde um alles zu speichern
     */
    public void stackFinished() {
        //Method called by the Panel to say that the stack is finished. So everything can be saved
    }

    /**
     * Gibt zurück, ob der Stapel noch Karten zum lernen hat.
     * @return Ob der Stapel noch Karten hat, die gelernt werden müssen.
     */
    public boolean hasCards() {
        return true;
    }

    /**
     * Macht das Lernen der letzen Karte bzw. InformationGroup rückgängig, z.B. wenn der Benutzer die falsche Rückmeldung gegeben hat.
     */
    public void redoLastCard() {

    }
}
