package informationManagement;

import utilities.Imprend;

import java.util.*;

/**
 * Repräsentiert eine Gruppe von {@link InfoObject}s, welche eine Karte darstellen soll. Die {@link InfoObject}s können in Gruppen unterteilt werden. {@link InfoObject}s in einer Gruppe, dienen nicht als Frage für eine InfoObject
 * der gleichen Gruppe. Damit können Synonyme realisiert werden. Pro Sprache sollte eine Gruppe erstellt werden.
 * Die InformationGroup enthält folgende Attribute: <br>
 *     -ArrayList<InfoObject> informations: Alle InfoObjects der InformationGroup. <br>
 *     -Date youngestDate: Das jüngste Abfragedatum der InformationGroup. Wird gebraucht um schnell zu erkennen, ob etwas in der InformationGroup abgefragt werden muss.  <br>
 *     -int id: Die ID der InformationGroup  <br>
 *     -String comment: Ein Kommentar zu der InformationGroup. Kann vom Benutzer verfasst werden.  <br>
 * Erstellt am 30.06.15
 */
public class InformationGroup {

    private ArrayList<InfoObject> informations;        //All InfoObjects in this InformationGroup. The index of each InfoObject should correspond with it's id.
    private Date youngestDate;                          //The youngest date existing in this InformationGroup. (Which Information should be asked for first)
    private String comment;                             //A comment to this InformationGroup, can by anything. The user will write it
    private int id;                                     //The id of the InformationGroup.
    // The ids in the InformationGroup will always be set by the InformationGroup, so every id in it is unique

    /**
     * Konrstruktor. Initialisiert alle Variablen mit Standardwerten
     */
    public InformationGroup() {
        informations = new ArrayList<>();
        youngestDate = null;
        //fill default values
        comment = "";
        id = -1;
    }

    /**
     * Gibt die Anzahl in InfoObjects in der InformationGroup zurück
     * @return Anzahl InfoObjects
     */
    public int getAmountInformations() {
        return  informations.size();
    }

    /**
     * Gibt das jüngste Abfragedatum zurück
     * @return Jüngstes Abfragedatum
     */
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

    /**
     * Liefert die kopierte ArrayList, mit allen InfoObjects zurück
     * @return kopierte InfoObjects
     */
    public ArrayList<InfoObject> copyAllInformations() {
        return (ArrayList<InfoObject>) informations.clone();
    }

    /**
     * Fügt ein InfoObject zur InformationGroup hinzu.
     * @param info  neues InfoObject
     */
    public void addInformation(InfoObject info) {
        info.setId(getNextId());
        info.setInfoGroupId(id);
        informations.add(info);
    }

    /**
     * Entfernt das InfoObject mit der gegebenen ID.
     * @param id  ID des zu entfernenden InfoObjects
     */
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

    /**
     * Ersetzt das InfoObject mit der gegebenen ID durch das gegebenen InfoObject
     * @param id  ID des zu ersetzenden InfoObjects
     * @param infoObject  Neues InfoObjects
     */
    public void replaceInfoObject(int id, InfoObject infoObject) {

        informations.set(id, infoObject);
    }

    /**
     * Gibt den Kommentar zurück
     * @return Kommentar
     */
    public String getComment() {
        return comment;
    }

    /**
     * Setzt den Kommentar
     * @param comment  Neuer Kommentar
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Liefert die ID der InformationGroup
     * @return ID der InformationGroup
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die ID der InformationGroup
     * @param id  neue ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt das  InfoObject mit der gegebenen ID zurueck
     * @param id  ID des verlangten InfoObjects
     * @return Verlangtes InfoObject
     */
    public InfoObject getInfoObjectById(int id) {
        //returns the InfoObject with the id given
        return informations.get(id);
    }

    private int getNextId() {
        //returns the next possible id.
        return informations.size();
    }
}

