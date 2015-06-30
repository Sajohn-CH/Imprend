package gui;

import utilities.Imprend;

import javax.swing.*;

/**
 * Created by samuel on 30.06.15.
 */
public class JSettingsPanel extends JNavPanel {

    public JSettingsPanel(Imprend imprend) {
        JLabel lbl = new JLabel("Settings");
        add(lbl);
    }

    @Override
    public void back(Imprend imprend) {
        imprend.switchPanel(imprend.strPnlMenu);
    }

}
