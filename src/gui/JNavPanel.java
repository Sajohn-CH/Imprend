package gui;

import utilities.Imprend;

import javax.swing.*;

/**
 * Erstellt am 30.06.15. Abstrakte Kindklasse von JPanel. Erweiter dies um 2 Methode, welche alle Panels, welche im Programm etwas anzeigen wolle (ausser die Leiste bzw. JBarPanel), haben müssen.
 * @author Samuel Martin
 */
public abstract class JNavPanel extends JPanel{

    /**
     * Konstruktor. Tut nichts.
     */
    public JNavPanel() {

    }

    /**
     * Diese Methode wird jedes Mal aufgerufen, wenn der Benutzer den Zurück-Knopf in der Leiste drückt. Es gibt true zurück, wenn das Panel nicht selber etwas rückgänig machen kann, dann wird zum letzen
     * Panel gewechselt. Wenn das Panel etwas rückgängig machen kann, gibt es false zurück und führt die Aktionen aus, die es Rückgängig machen kann. Falls das Panel
     * die Methode nicht überschreibt wird diejenige vom JNavPanel ausgeführt, welche true zurückgibt.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     * @return Ob zu vorherigen Panel geswicht werden soll (ja = true).
     */
    public boolean back(Imprend imprend) {
        //Returns true, if Imprend should switch to the last opened panel (when the panel can't undo anything by itself). Should include any actions which the panel needs to do, before being close
        //e.g. saving
        //Returns false, when it can perform the going back action itself (when the panel can undo anything by itself).
        return true;
    }

    /**
     * Die Funktion wird jedes Mal aufgerufen, wenn das Panel geschlossen wird. Dann kann es Aktionen ausführen, die es machen will/muss, bevor es geschlosse wird, bzw. ein anderes Panel in den
     * Vordergrund kommt. Dies kann z.B: zum speichern von Inhalten genutzt werden. Wenn das Panel diese Methode nicht überschreibt, wird nichts getan.
     * @param imprend   Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    public void cleanUp(Imprend imprend) {
        //This method is called when the panel is closed. The panel should save things here.
    }
}
