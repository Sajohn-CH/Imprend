package utilities;

import questionMethods.QuestionMethod;
import gui.*;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

/**
 * "Hauptklasse" des Programms. Beinhaltet die main-Methode. Beinhaltet alle Panel der Grapischen Oberfläche, welche alle je einem String zugeordnet sind, um zu diesem Panel zu gelangen.
 * Ebenfalls beinhaltet es statische Variablen, die im gesamten Programm gebraucht werden. Auch regelt es den Fall, dass das vorherige Panel angezeigt werden soll, wenn der Zurück-Knopf gedrückt wurde.
 * Das Hauptfenster des Progamms hat ein BorderLayout, bei dem oben die Leiste (BorderLayout.PAGE_START) {@link JBarPanel} und unten (bei BoderLayout.CENTER) das pnlMain ist, welches nur als Behälter für
 * alle anderen Panel dient. Diese sind im pnlMain mit einem CardLayout angeordnet. Dies ermöglicht immer nur ein Panel auf dem pnlMain anzuzeigen. Dafür werden die Strings benötigt die immer einem Panel
 * zugeordnet sind. <br>
 * Manche Panels werden erst später initialisiert und dem pnlMain hinzugefügt, weil dies mit bestimmten Parameter initialisiert werden müssen (z.B: {@link JEditPanel}). Diese können dann mit der öffentlichen
 * Methode {@link Imprend#addPanelToMain(JNavPanel, String)} hinzugefügt werden. Diese Panels besitzen hier aber trotzdem eine Variabel, welche dann aber noch nicht initialisiert wurde
 *.Dies geschieht dann mit der entsprechenden Setter-Methode. <br>
 * Erstellt am 30.06.15
 * @author Samuel Martin
 */

public class Imprend {

    private static Imprend imprend;
    private JFrame frame;
    private CardLayout cd;

    /**
     * Variable, die alle Einstellungen des Programms beinhaltet. Siehe {@link Settings}.
     */
    public Settings settings;

    private JAddPanel pnlAdd;
    private JEditPanel pnlEdit;
    private JPanel pnlMain;
    private JBarPanel pnlBar;
    private JCardPanel pnlCard;
    private JWrittenCardPanel pnlCardWritten;
    private JMenuPanel pnlMenu;
    private JSettingsPanel pnlSettings;

    //all Strings for the names of each panel for the cardLayout
    /** String für das Hauptmenu-Panel ({@link JMenuPanel}). Wird beim Wechseln von Panels benötigt.*/
    public final String strPnlMenu = "JMenuPanel";
    /** String für das Einstellungs-Panel ({@link JSettingsPanel}). Wird beim Wechseln von Panels benötigt*/
    public final String strPnlSettings = "JSettingsPanel";
    /** String für das Kartenlernen-Panel ({@link JCardPanel}). Wird beim Wechseln von Panels benötigt*/
    public final String strPnlCard = "JCardPanel";
    /** String für das Stapel-Hinzufügen-Panel ({@link JAddPanel}). Wird beim Wechseln von Panels benötigt*/
    public final String strPnlAdd = "JAddPanel";
    /** String für das Stapel-Bearbeiten-Panel ({@link JEditPanel}). Wird beim Wechseln von Panels benötigt*/
    public final String strPnlEdit = "JEditPanel";
    /** String für das Schriftlich-Kartenlernen-Panel ({@link JWrittenCardPanel}). Wird beim Wechseln von Panels benötigt*/
    public final String strPnlCardWritten = "pnlCardWritten";

    //all String for the different types of InfoObjects
    /**
     * String, der benötigt wird, um festzustellen ob ein InfoObject ein Information-Object ist.
     */
    public static final String strInfoObjectInfo = "Information";
    /**
     * String, der benötigt wird, um festzustellen ob ein InfoObject ein Question-Object ist.
     */
    public static final String strInfoObjectQuest = "Question";

    private static String currentPanel;     //name of the current panel beeing visible
    private static Map<String, JNavPanel> panels = new HashMap<>();    //map of all panels and their names, to associate them with each other
    private ArrayList<String> panelLog;     //ArrayList with the order of the last opened panels. (Used for back button). It is used as an Stack

    /**
     * Main-Methode des Programms. Diese wird als Erstes aufgerufen, wenn das Programm gestartet wurde. Ruft nur die init()-Methode auf, welche dann die gesamte graphische Oberfläche initialisiert
     * und startet.
     * @param args  Parameter, die übergeben werden, wenn es aus der Kommandozeile gestartet wird. (Wird hier nicht benützt)
     */
    public static void main(String[] args) {
        init();
    }

    private static void init() {
        //initialise objects
        imprend = new Imprend();
        imprend.cd = new CardLayout();
        imprend.frame = new JFrame();
        imprend.settings = new Settings(new File("resources" + File.separator + "settings.properties"));

        //Load all fonts (for the panels)
        imprend.loadFonts();

        //initialize the panels
        imprend.pnlBar = new JBarPanel(imprend);
        imprend.pnlMain = new JPanel();
        imprend.pnlMenu = new JMenuPanel(imprend);
        imprend.pnlSettings = new JSettingsPanel(imprend);
        imprend.pnlCard = new JCardPanel(imprend);
        imprend.pnlCardWritten = new JWrittenCardPanel(imprend);

        imprend.panelLog = new ArrayList<>();

        //initilise pnlMain and add the other panels to it. Some panels will be added later with the addPanelToMain() method.
        imprend.pnlMain.setLayout(imprend.cd);
        imprend.addPanelToMain(imprend.pnlMenu, imprend.strPnlMenu);
        imprend.addPanelToMain(imprend.pnlSettings, imprend.strPnlSettings);
        imprend.addPanelToMain(imprend.pnlCard, imprend.strPnlCard);
        imprend.addPanelToMain(imprend.pnlCardWritten, imprend.strPnlCardWritten);

        //add WindowListener the frame, so the cleanUp-Method of the current Panel will be called. This happens with the
        //local pnlCleaUp()-Method.
        WindowListener goClose = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                //let the currently opened panel do some cleanUp
                imprend.pnlCleanUp();

                System.exit(0);
            }
        };

        //set Size of frame to fullscreen, make it visible, add WindowListener and add Panels.
        imprend.frame.setSize(800, 600);
        //make frame fullscreen
        imprend.frame.setExtendedState(imprend.frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        imprend.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        imprend.frame.addWindowListener(goClose);
        imprend.frame.setVisible(true);
        imprend.frame.setLayout(new BorderLayout());
        imprend.frame.add(imprend.pnlBar, BorderLayout.PAGE_START);
        imprend.frame.add(imprend.pnlMain, BorderLayout.CENTER);

        //change the language within the JOptionPanes
        ResourceBundle resource = imprend.settings.getResourceBundle();
        UIManager.put("OptionPane.cancelButtonText", resource.getString("Cancel"));
        UIManager.put("OptionPane.noButtonText", resource.getString("No"));
        UIManager.put("OptionPane.okButtonText", resource.getString("Ok"));
        UIManager.put("OptionPane.yesButtonText", resource.getString("Yes"));

        //Set the Panel, which should be displayed first, the currentPanel is needed to be set once before, because the switchPanel-Method will call the
        //pnlCleanUp()-Method which will call the cleanUp()-Method of the last panel (=currentPanel).
        currentPanel = imprend.strPnlMenu;
        imprend.switchPanel(imprend.strPnlMenu);
    }

    /**
     * Methode, die aufgerufen wird, um ein Panel dem Programm (bzw. den JFrame) hinzuzufügen.
     * @param pnl  String, welches später benötigt wird, um das Panel anzuzeigen
     * @param strPnl  Panel, welches hinzugefügt werden soll.
     */
    public void addPanelToMain(JNavPanel pnl, String strPnl) {
        imprend.pnlMain.add(pnl, strPnl);
        panels.put(strPnl, pnl);
    }

    /**
     * Ruft das Panel auf, das mit dem gegebenen String verknüpft (zusammen mit {@link Imprend#addPanelToMain(JNavPanel, String)} hinzugefügt) wurde.
     * Ruft automatisch bei jeden Wechsel die Methode {@link Imprend#pnlCleanUp()} auf, die es dem aktuellem Panel ermöglich alles zu speichern.
     * Beinhaltet ebenfalls eine swithc-Abfrage, die bei gewissen Panels Methoden aufrufen, wenn diese etwas vorladen müssen. Aktuell macht dies
     * das {@link JMenuPanel}, um die Stapelliste erneut zu laden.
     * @param strPanel  String, welches mit dem Panel verknüpft ist.
     */
    public void switchPanel(String strPanel) {
        //first call the cleanUp()-Method of the panel, which will be closed now
        pnlCleanUp();
        //Show the new Panel
        imprend.cd.show(imprend.pnlMain, strPanel);
        currentPanel = strPanel;

        //add panel to panelLog
        panelLog.add(strPanel);

        //add here things to be done, when certain panels are loaded
        switch(strPanel) {
            case strPnlMenu:
                imprend.pnlMenu.reloadStackList(imprend);
                break;
        }
    }

    /**
     * Gibt das Panel, welches für das Abfragen von Karten zuständig ist, aus.
     * @return JCardPanel
     */
    public JCardPanel getPnlCard() {
        return pnlCard;
    }
    /**
     * Gibt das Panel, welches für das schriftliche Abfragen von Karten zuständig ist, aus.
     * @return JWrittenCardPanel
     */
    public JWrittenCardPanel getPnlCardWritten() {
        return pnlCardWritten;
    }

    /**
     * Gibt das Panel, welches als Startmenu dient, aus
     * @return JMenuPanel
     */
    public JMenuPanel getPnlMenu() {
        return pnlMenu;
    }

    /**
     * Gibt das Panel, welches zum Hinzufügen von Stapeln dient, zurück
     * @return JAddPanel
     */
    public JAddPanel getPnlAdd() {
        return pnlAdd;
    }

    /**
     * Setzt das Panel, welches zum Hinzufügen von Stapeln dient. Dies ist nötig, da für jeden neuen Stapeln, ein eigenes Panel generiert wird.
     * @param pnlAdd  Neues JAddPanel
     */
    public void setPnlAdd(JAddPanel pnlAdd) {
        this.pnlAdd = pnlAdd;
    }

    /**
     * Gibt das Panel, welches zum Bearbeiten von Stapeln dient, zurück.
     * @return JEditPanel
     */
    public JEditPanel getPnlEdit() {
        return pnlEdit;
    }

    /**
     * Setzt das Panel, welches zum Bearbeiten von Stapeln dient. Dies ist nötig, da für jeden Stapeln, ein eigenes Panel generiert wird.
     * @param pnlEdit  Neues JEditPanel
     */
    public void setPnlEdit(JEditPanel pnlEdit) {
        this.pnlEdit = pnlEdit;
    }

    /**
     * Gibt das JFrame zurück, welches das Hauptfenster des Programms darstellt.
     * @return Hauptfenster des Programms.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Wird aufgerufen, wenn der Benutzer den Zurückknopf in der Leiste ({@link gui.JBarPanel}) drückt.
     * Entweder kann das aktuelle Panel selber etwas rückgängig machen, oder das vorherige Panel wird angezeigt. Um herauszufinden, was zu tun ist, wird die back()-Methode des akutuellen Panels
     * aufgerufen.
     */
    public void back() {
        //it back() returns false, the panel can go back itself, it doesn't want to switch panel
        //back() also includes any actions the panel may want to do before it goes back (eg. saving).
        if(panels.get(currentPanel).back(imprend)) {
            //switch to last opened panel (which is the second last added panel to panelLog, the last one is the current)
            switchPanel(panelLog.get(panelLog.size()-2));
            //remove two panels from the panel log, the one just switched in the process of going back and one time the current one, because, otherwise the current panel (the one just switched to)
            //would occur twice, once as switched now to, and the other one before the going back. The seconde panel removed is the one, the user is comming back from.
            panelLog.remove(panelLog.size()-1);
            if(panelLog.size() != 1) {
                panelLog.remove(panelLog.size()-1);
            }
        }
    }

    /**
     * Wird aufgerufen wenn entweder das Programm geschlossen wird, oder zum Hauptmenu zurückgekehrt wird.
     * Gibt dem aktuellen Panel die Möglichkeit alles zu speichern, Auch werden die Einstellungen gespeichert.
     */
    public void pnlCleanUp() {
        panels.get(currentPanel).cleanUp(imprend);
        settings.saveSettings();
    }

    /**
     * Lädt aktuellen Fonts aus den Einstellungen. Wird beim Starten einmal aufgerufen und wenn man das Einstellungsmenu verläst.
     */
    public void loadFonts() {
        //Enumeration and to put the Look and Feel based on http://stackoverflow.com/questions/7434845/setting-the-default-font-of-swing-program (seen at 29.11.15)
        //Load Font
        Font font = imprend.settings.getTextFont();
        //create new FontUIResource
        FontUIResource res = new FontUIResource(font);

        Enumeration test = UIManager.getDefaults().keys();

        //put the font into the Look and Feel, so the font will be used everywhere
        while (test.hasMoreElements()) {
            Object key = test.nextElement();
            Object value = UIManager.get(key);
            if ( value instanceof Font ) {
                UIManager.getLookAndFeelDefaults().put(key, res);
            }
        }
        //Update, so the font will be directly used.
        SwingUtilities.updateComponentTreeUI(imprend.frame);
    }

    /**
     * Lädt die Tastaturbelegung von den Panels, die eine haben, aus den Einstellungen. Wird aufgerufen, wenn das Einstellungsmenu verlassen wird.
     * Die Panels laden ihre Tastaturbelegung immer beim Initialisieren, im Konstruktor, selber.
     */
    public void loadKeyBindings() {
        pnlCard.loadKeyBindings(imprend);
        pnlCardWritten.loadKeyBindings(imprend);
    }

}
