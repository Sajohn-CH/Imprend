package informationManagement;

import utilities.Load;

import java.io.File;
import java.util.ArrayList;

/**
 * Stellt einen Stapel mit Informationen zum lernen dar. Es besitzt folgende Attribute: <br>
 *     -ArrayList<InformationGroup> infoGroups: Alle InformationGroups des Stapels <br>
 *     -String name: Name des Stapels  <br>
 *     -File stackFile: Datei, in der der Stapel abgespeichert ist. <br>
 * Erstellt am 30.06.15
 *
 * @author Samuel Martin
 */
public class Stack {
    private ArrayList<InformationGroup> infoGroups;     //All informationGroups. The index of each InformationGroup should correspond with it's id.
    private String name;                                //The name of the Stack
    private File stackFile;                             //The file, where all informations of the stack are stored

    /**
     * Konstrukor. Verlangt den Pfad der Datei, in der der Stapel gespeichert werden soll. Der Name des Stapels wird aus der Pfad abgelesen. Die Datei wird nach dem Stapel benannt.
     * @param stackPath - Pfad des Stapels
     */
    public Stack(String stackPath) {
        //get the stackname out of the stackpath. The stackpath is the path to the folder where the stack lies + stackname + .xml or .txt or whatever type it is
        String[] partName;
        //This if-loop is needed, because the File.seperator for windows will be "\", but this will be interpreted as an escape character. To finally get the normal "\"
        //I need to have it four times for an regrex, but only twice in an normal String. But File.seperator only gives me a "\" back.
        if(File.separator.equals("\\")) {
            //The OS is windows
            partName = stackPath.split("\\\\");
        } else {
            partName = stackPath.split(File.separator);
        }
        name = partName[partName.length-1];
        //remove the .xml ending
        String[] arrayName = name.split("\\.");
        name = "";
        for(int i = 0; i < arrayName.length-2; i++) {
            name += arrayName[i] + ".";
        }
        name += arrayName[arrayName.length-2];

        //Load Stack out of File
        stackFile = new File(stackPath);
        if(stackFile.exists()) {
            //Only load the stack if it exists. So there won't be any errors, when e.g. e new stack is created, which first need to be "filled" with information.
            infoGroups = Load.loadStack(stackFile);
        } else {
            infoGroups = new ArrayList<>();
        }
    }

    /**
     * Liefert die Anzahl an InformationGroups in dem Stapel zurück
     * @return Anzahl InformationGroups
     */
    public int getAmountInformationGroups() {
        return infoGroups.size();
    }

    /**
     * Kopiert alle InformationGroups des Stapels und gibt diese zurück
     * @return Alle InformationGroups
     */
    public ArrayList <InformationGroup> copyAllInformationGroups() {
        return (ArrayList<InformationGroup>) infoGroups.clone();
    }

    /**
     * Gibt alle InformationGroups zurück, die gelernt werden müssen. Das sind alle, deren jüngstes Datum vor dem akutellem Datum ist.
     * @return Alle InformationGroups, die gelernt werden müssen.
     */
    public ArrayList<Integer> getAllInfoGroupsToLearn() {
        //returns all the ids fo all InformationGroups, where the youngest date (to learn next) is before the current date
        ArrayList<Integer> infoGroupsToLearn = new ArrayList<>();
        //get currentTime in msec (since the 1.1.1970, I think)
        Long currentTime = System.currentTimeMillis();
        for(int i = 0; i < infoGroups.size(); i++) {
            //compare each youngest Date to the current date. If the youngest is before the current, then the information will need to be learned
            long timeDiff = currentTime - infoGroups.get(i).getYoungestDate().getTime();
            if(timeDiff >= 0) {
                infoGroupsToLearn.add(infoGroups.get(i).getId());
            }
        }
        return infoGroupsToLearn;
    }

    /**
     * Gibt den Name des Stack zurück.
     * @return Name des Stacks
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt Datei, in der der Stapel gespeichert ist zurück
     * @return Datei, in der der Stapel gespeichert ist.
     */
    public File getStackFile() {
        return stackFile;
    }

    /**
     * Liefert ein bestimmtes InfoObject mit der gegebenen ID.
     * @param infoGroupId  ID der InformationGroup des InfoObjects
     * @param infoObjectId  ID des InfoObjects in der InformationGroup
     * @return InfoObject mit der gegebenen ID.
     */
    public InfoObject getInfoObjectById(int infoGroupId, int infoObjectId) {
        //returns the InfoObject with the given id. The id of each InfoObject must be unique (among other InfoObjects) in each stack.
        //InfoObject = Information or Question
        return getInfoGroupById(infoGroupId).getInfoObjectById(infoObjectId);
    }

    /**
     * Überladung der Methode getInfoObjectById(int infoGroupId, int infoObjectI) mit der ID als Array statt zwei einzelne Parameter
     * @param ids  ID in Arrayform. [ID der InformationGroup, ID des InfoObject]
     * @return InfoObject mit der gegebenen ID.
     */
    public InfoObject getInfoObjectById(Integer[] ids) {
        //returns the InfoObject with the given id. The id of each InfoObject must be unique (among other InfoObjects) in each stack.
        //InfoObject = Information or Question
        //ids[0] = id of InfoGroup
        //ids[1] = id of InfoObject
        return getInfoGroupById(ids[0]).getInfoObjectById(ids[1]);
    }

    /**
     * Liefert InformationGroup der gegeben ID.
     * @param id  ID der InformationGroup
     * @return InformationGroup mit der gegebenen ID.
     */
    public InformationGroup getInfoGroupById(int id) {
        //returns the Informationgroup with the given id. The id of each InformationObject must be unique (among other InformationGroups) in each stack.;
        return infoGroups.get(id);
    }

    /**
     * Fügt die gegebene InformationGroup zum Stapel hinzu.
     * @param informationGroup  zu hinzufügenden InformationGroup
     */
    public void addInformationGroup(InformationGroup informationGroup) {
        informationGroup.setId(getNextId());
        infoGroups.add(informationGroup);
    }

    /**
     * Entfernt die InformationGroup mit der gegebenen ID.
     * @param id  ID der zu entfernenden InformationGroup
     */
    public void removeInformationGroup(int id) {
        if(id >= infoGroups.size()) {
            //id doens't exits
            System.out.println("ERROR: Id doesn't exits. Can't delete InfoObject");
            return;
        }
        //removes an object and looks that all other id, greater than the deleted one are moving up
        infoGroups.remove(id);
        for(int i = id; i < infoGroups.size(); i++) {
            infoGroups.get(i).setId(i);
        }
        return;
    }

    private int getNextId() {
        //returns the next possible id.
        return infoGroups.size();
    }
}
