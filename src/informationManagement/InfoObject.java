package informationManagement;

/**
 * Abstrakte Vaterklasse für Information und Question-Objekte. Kann genutzt werden um weitere Objekte ähnlicher Art zu erstellen.
 * Es besitzt folgenden Attribute: <br>
 *     -String information: Hier ist die eigentliche Information gespeichert. <br>
 *     -int id: Die ID der InfoObjects <br>
 *     -int infoGroupId: Die ID der InformationGruppe, welcher das InfoObject angehört. <br>
 *     -String group: Gruppe, welcher dieses InfoObject angehört. Die Gruppe kann gebraucht werden, um InfoObjects in einer InformationGroup zu unterteilen, um z.B. für jede Sprache eine Gruppe zu erstellen. <br>
 * Erstellt am 08.07.15.
 * @author Samuel Martin
 */
public abstract class InfoObject {

    private String information;     //The Information itself
    private int id;                 //The id of this InfoObject
    private int infoGroupId;        //the id of the InformationGroups the Information belongs to
    private String group;           //The group of the information. Used to divide InfoObjects in the InformationGroup into groups. e.g. for each language one

    /**
     * Konstruktor. Initialisert alle Variablen mit Standardwerten.
     */
    public InfoObject() {
        //fill default values
        information = "";
        id = -1;
        infoGroupId = -1;
        group = "0";
    }

    /**
     * Gibt die Information zurück
     * @return Information
     */
    public String getInformation() {
        return information;
    }

    /**
     * Setzt die Information auf den gegebenen Wert
     * @param info  Neue Information
     */
    public void setInformation(String info) {
        this.information = info;
    }

    /**
     * Gibt die ID zurück
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die ID
     * @param id  Neue ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Liefert den "Typ" zurück. Dies ist der Klassennamen ohne die Dateiendung.
     * Wird gebraucht um bei InfoObjets zu entscheiden welche Klasse (Information oder Question) sie sind.
     * @return String Klassennamen / Typ
     */
    public String getType () {
        String name = getClass().getName();
        String[] array = name.split("\\.");
        return array[array.length-1];
    }

    /**
     * Gibt die ID der InformationGroup, welcher das InfoObject angehört, zurück
     * @return Id der InformationGroup
     */
    public int getInfoGroupId() {
        return infoGroupId;
    }


    /**
     * Setzt die ID der InformationGroup, welcher das InfoObject angehört.
     * @param infoGroupId  Neue ID der InformationGroup
     */
    public void setInfoGroupId(int infoGroupId) {
        this.infoGroupId = infoGroupId;
    }

    /**
     * Setzt die Gruppe
     * @param group  Neue Gruppe des InfoObjects (Ist ein String)
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Gibt die Gruppe zurück
     * @return Gruppe des InfoObjects (Ist ein String)
     */
    public String getGroup() {
        return group;
    }
}
