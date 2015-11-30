package informationManagement;

/**
 * Klasse repräsentiert eine Frage. Das heisst, dass es nicht erfragt werden kann. Es hat keine zusätzlichen Methoden und Attribute im Verlgeich zum {@link InfoObject}.
 * Diese Klasse exisitiert nur damit man später einfacher etwas nur an der Question ändern kann, ohne das InfoObject zu ändern. <br>
 * Erstellt am 30.06.15
 * @author Samuel Martin
 */

 //Created by samuel on 30.06.15.
public class Question extends InfoObject{
    /**
     * Konstruktor, ruft nur den Konstruktor von {@link InfoObject} auf.
     */
    public Question() {
        super();
    }
}
