package informationManagement;

import utilities.Load;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by samuel on 30.06.15.
 */
public class Stack {
    private ArrayList<InformationGroup> infoGroups;
    private String name;
    private File stackFile;
    private int id;

    public Stack(String stackPath) {
        //get the stackname out of the stackpath. The stackpath is the path to the folder where the stack lies + stackname + .xml or .txt or whatever type it is
        String[] partName = stackPath.split(File.separator);

        stackFile = new File(stackPath);
        if(stackFile.exists()) {
            //Only load the stack if it exists. So there won't be any errors, when e.g. e new stack is created, which first need to be "filled" with information.
            infoGroups = Load.loadStack(stackFile);
            id = Load.getStackId(stackFile);
        } else {
            infoGroups = new ArrayList<>();
        }
        name = partName[partName.length-1];
        name = name.split("\\.")[0];
    }

    public ArrayList<InformationGroup> getAllInfoGroups() {
        return infoGroups;
    }

    public ArrayList<InformationGroup> getAllInfoGroupsToLearn() {
        //returns all InformationGroups, where the youngest date (to learn next) is before the current date
        ArrayList<InformationGroup> infoGrpsToLearn = new ArrayList<>();
        //get currentTime in msec (since the 1.1.1970, I think)
        Long currentTime = System.currentTimeMillis();
        for(int i = 0; i < infoGroups.size(); i++) {
            //compare each youngest Date to the current date. If the youngest is before the current, then the information will need to be learned
            long timeDiff = currentTime - infoGroups.get(i).getYoungestDate().getTime();
            if(timeDiff >= 0) {
                infoGrpsToLearn.add(infoGroups.get(i));
            }
        }
        return infoGrpsToLearn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getStackFile() {
        return stackFile;
    }

    public int getId() {
        return id;
    }

    public InfoObject getInfoObjectById(int id) {
        //returns the InfoObject with the given id. The id of each InfoObject must be unique in each stack.
        //InfoObject = Information or Question
        for(int i = 0; i < infoGroups.size(); i++) {
            if(infoGroups.get(i).getInfoObjectById(id) != null) {
                return infoGroups.get(i).getInfoObjectById(id);
            }
        }
        return null;
    }

    public void addInformationGroup(InformationGroup informationGroup) {
        infoGroups.add(informationGroup);
    }
}
