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

    public Stack(String stackPath) {
        stackFile = new File(stackPath);
        infoGroups = Load.loadStack(stackFile);
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
}
