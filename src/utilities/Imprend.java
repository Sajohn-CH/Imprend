package utilities;

import gui.JBarPanel;
import gui.JMenuPanel;
import gui.JNavPanel;
import gui.JSettingsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samuel on 30.06.15.
 */
public class Imprend {

    private static Imprend imprend;
    private JFrame frame;
    private CardLayout cd;

    public Settings settings;

    private JPanel pnlMain;
    private JBarPanel pnlBar;

    //all Strings for the names of each panel for the cardLayout
    public String strPnlMenu = "JMenuPanel";
    public String strPnlSettings = "JSettingsPanel";

    private static String currentPanel;     //name of the current panel beeing visible
    private static Map<String, JNavPanel> panels = new HashMap<>();    //map of all panels and their names, to associate them with each other

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        imprend = new Imprend();
        imprend.cd = new CardLayout();
        imprend.frame = new JFrame();
        imprend.settings = new Settings();

        imprend.settings.setCardsDir(new File("resources" + File.separator + "cards"));

        imprend.pnlBar = new JBarPanel(imprend);

        imprend.pnlMain = new JPanel();
        JMenuPanel pnlMenu = new JMenuPanel(imprend);
        JSettingsPanel pnlSettings = new JSettingsPanel(imprend);


        imprend.pnlMain.setLayout(imprend.cd);
        imprend.addPanelToMain(pnlMenu, imprend.strPnlMenu);
        imprend.addPanelToMain(pnlSettings, imprend.strPnlSettings);


        imprend.frame.setSize(1920,1080);
        imprend.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imprend.frame.setVisible(true);
        imprend.frame.setLayout(new BorderLayout());
        imprend.frame.add(imprend.pnlBar, BorderLayout.PAGE_START);
        imprend.frame.add(imprend.pnlMain, BorderLayout.CENTER);
        switchPanel(pnlMenu.getName());

    }

    private void addPanelToMain(JNavPanel pnl, String strPnl) {
        imprend.pnlMain.add(pnl, strPnl);
        panels.put(strPnl, pnl);
    }

    public static void switchPanel(String strPanel) {
        imprend.cd.show(imprend.pnlMain, strPanel);
        currentPanel = strPanel;
    }

    public void back() {
        panels.get(currentPanel).back(imprend);
    }
}
