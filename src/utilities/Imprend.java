package utilities;

import questionMethods.QuestionMethod;
import gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by samuel on 30.06.15.
 */
public class Imprend {

    private static Imprend imprend;
    public JFrame frame;
    private CardLayout cd;

    public Settings settings;

    public JAddPanel pnlAdd;
    private JPanel pnlMain;
    private JBarPanel pnlBar;
    private JCardPanel pnlCard;
    private JMenuPanel pnlMenu;

    //all Strings for the names of each panel for the cardLayout
    public final String strPnlMenu = "JMenuPanel";
    public final String strPnlSettings = "JSettingsPanel";
    public final String strPnlCard = "JCardPanel";
    public final String strPnlAdd = "JAddPanel";

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

        //initialize the panels
        imprend.pnlMain = new JPanel();
        imprend.pnlMenu = new JMenuPanel(imprend);
        JSettingsPanel pnlSettings = new JSettingsPanel(imprend);
        imprend.pnlCard = new JCardPanel(imprend);
        imprend.pnlAdd = new JAddPanel();   //This JAddPanel doesn't get imprend because it is more like a placeholder. If one want to switch to this panel, he sould make a new one, with
                                            //imprend and a stack, which it should fill with content (with: imprend.pnlAdd = new JAddPanel(imprend, stack);


        imprend.pnlMain.setLayout(imprend.cd);
        imprend.addPanelToMain(imprend.pnlMenu, imprend.strPnlMenu);
        imprend.addPanelToMain(pnlSettings, imprend.strPnlSettings);
        imprend.addPanelToMain(imprend.pnlCard, imprend.strPnlCard);


        imprend.frame.setSize(imprend.settings.getResolution());
        imprend.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imprend.frame.setVisible(true);
        imprend.frame.setLayout(new BorderLayout());
        imprend.frame.add(imprend.pnlBar, BorderLayout.PAGE_START);
        imprend.frame.add(imprend.pnlMain, BorderLayout.CENTER);

        //change the language within the JOPtionPanels
        ResourceBundle general = ResourceBundle.getBundle(imprend.settings.getResourceBundles()+".GeneralBundle", imprend.settings.getLocale(), new UTF8Control());
        UIManager.put("OptionPane.cancelButtonText", general.getString("Cancel"));
        UIManager.put("OptionPane.noButtonText", general.getString("No"));
        UIManager.put("OptionPane.okButtonText", general.getString("Ok"));
        UIManager.put("OptionPane.yesButtonText", general.getString("Yes"));


        //Set the Panel, which should be displayed first
        imprend.switchPanel(imprend.pnlMenu.getName());
    }

    public void addPanelToMain(JNavPanel pnl, String strPnl) {
        imprend.pnlMain.add(pnl, strPnl);
        panels.put(strPnl, pnl);
    }

    public void switchPanel(String strPanel) {
        imprend.cd.show(imprend.pnlMain, strPanel);
        currentPanel = strPanel;
    }

    public void JCardPanel_initNewLearning(QuestionMethod questionMethod) {
        //This method only is there to allow the pnlMenu to call the initNewLearning()-Method from the pnlCard
        imprend.pnlCard.initNewLearning(questionMethod);
    }

    public void JMenuPanel_reloadStackList(Imprend imprend) {
        pnlMenu.reloadStackList(imprend);
    }

    public void back() {
        panels.get(currentPanel).back(imprend);
    }
}
