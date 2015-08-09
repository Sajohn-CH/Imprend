package informationManagement;

import utilities.Imprend;
import utilities.Load;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by samuel on 30.06.15.
 */
public class Stack {
    private ArrayList<InformationGroup> infoGroups;     //All informationGroups. The index of each InformationGroup should correspond with it's id.
    private String name;                                //The name of the Stack
    private File stackFile;                             //The file, where all informations of the stack are stored

    public Stack(String stackPath) {
        //get the stackname out of the stackpath. The stackpath is the path to the folder where the stack lies + stackname + .xml or .txt or whatever type it is
        String[] partName = stackPath.split(File.separator);

        stackFile = new File(stackPath);
        if(stackFile.exists()) {
            //Only load the stack if it exists. So there won't be any errors, when e.g. e new stack is created, which first need to be "filled" with information.
            infoGroups = Load.loadStack(stackFile);;
        } else {
            infoGroups = new ArrayList<>();
        }
        name = partName[partName.length-1];
        name = name.split("\\.")[0];
    }

    public int getAmountInformationGroups() {
        return infoGroups.size();
    }

    public ArrayList <InformationGroup> copyAllInformationGroups() {
        return (ArrayList<InformationGroup>) infoGroups.clone();
    }

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

    public String getName() {
        return name;
    }

    public File getStackFile() {
        return stackFile;
    }

    public InfoObject getInfoObjectById(int infoGroupId, int infoObjectId) {
        //returns the InfoObject with the given id. The id of each InfoObject must be unique (among other InfoObjects) in each stack.
        //InfoObject = Information or Question
        return getInfoGroupById(infoGroupId).getInfoObjectById(infoObjectId);
    }

    public InfoObject getInfoObjectById(Integer[] ids) {
        //returns the InfoObject with the given id. The id of each InfoObject must be unique (among other InfoObjects) in each stack.
        //InfoObject = Information or Question
        //ids[0] = id of InfoGroup
        //ids[1] = id of InfoObject
        return getInfoGroupById(ids[0]).getInfoObjectById(ids[1]);
    }

    public InformationGroup getInfoGroupById(int id) {
        //returns the Informationgroup with the given id. The id of each InformationObject must be unique (among other InformationGroups) in each stack.;
        return infoGroups.get(id);
    }

    public void addInformationGroup(InformationGroup informationGroup) {
        informationGroup.setId(getNextId());
        infoGroups.add(informationGroup);
    }

    public void removeInformationGroup(int id) {
        if(id >= infoGroups.size()) {
            //id doens't exits
            System.out.println("EROROR: Id doesn't exits. Can't delete InfoObject");
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
