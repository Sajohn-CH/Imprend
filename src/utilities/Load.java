package utilities;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by samuel on 30.06.15.$
 * Static class with methods to load different files, which the program needs to run. E.g. the files containing the cards.
 */
public class Load {

    public static String[] getAllObjectPathsIn(File dir) {
        //returns all the paths of all the objects in the given directory
        if(!dir.isDirectory()) {
            return null;
        }
        File[] files = dir.listFiles();
        String[] paths = new String[files.length];
        for(int i = 0; i < files.length; i++) {
            paths[i] = files[i].getPath();
        }
        return paths;
    }
}
