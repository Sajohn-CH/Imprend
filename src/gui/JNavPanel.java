package gui;

import utilities.Imprend;

import javax.swing.*;

/**
 * Created by samuel on 30.06.15.
 */
public class JNavPanel extends JPanel{

    public JNavPanel() {

    }

    public boolean back(Imprend imprend) {
        //Returns true, if Imprend should switch to the last opened panel (when the panel can't undo anything by itself). Should include any actions which the panel needs to do, before being close
        //e.g. saving
        //Returns false, when it can perform the going back action itself (when the panel can undo anything by itself).
        return true;
    }

    public void cleanUp(Imprend imprend) {
        //This method is called when the panel is closed. The panel should save things here.
    }
}
