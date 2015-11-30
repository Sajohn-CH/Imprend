package gui;

import informationManagement.Information;
import informationManagement.InformationGroup;
import informationManagement.Question;
import informationManagement.Stack;
import utilities.Imprend;
import utilities.Save;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/**
 * Diese Klasse ist eine Kindklasse von {@link JNavPanel} und beinhaltet alles Mögliche um einen neuen Stapel zu erstellen oder InformationGroups (also Karten) zu einem bestehnden Stapel hinzuzufügen.
 * Es beinhaltet folgende Elemente: <br>
 * -Textfelder um eine Karte mit Vorder- und Rückseite zu erstellen. Die Karte kann beidseitig benutzt werden (Beide Seite können Frage und Antwort sein).
 *  Auch können zur jeder Kartenseite beliebig viele Synonyme hinzugefügt werden. <br>
 * - Ein Feld um ein Kommentar zu einer Karte zu verfasen <br>
 * - Schon verfasste Karten werden in eine Liste angezeigt und können, per Mausklick, bearbeitet werden (mit dem {@link JEditPanel}) <br>
 * - Es exisitiert ein modifizierbarer Tastenkürzel, der nacheinander alle Felder, die nötig sind um eine Karte zu erstellen, durchgeht. Standardmässig ist dies die Entertaste. <br>
 * Erstellt am 03.07.15.
 * @author Samuel Martin
 * {@inheritDoc}
 */

public class JAddPanel extends JNavPanel implements MouseListener{
    private DefaultListModel<String> lstModel;          //ListModel of lstCards. Is global so the content can be changed in other method (reloadCardsList())
    private JButton btnAddSyn;              //Button to add Synonyms
    private ArrayList<JTextField> side1;    //all JTextFields of the first side (of the card)
    private JScrollPane scrlPnSide1;        //the JScrollPane for the first side
    private JPanel pnlSide1;                //the JPanel for all the JTextFields of the first side
    private JButton btnAddSynToAnw;         //Button to add Synonyms to the answer
    private ArrayList<JTextField> side2;    //all JTextFields of the second side (of the card)
    private JScrollPane scrlPnSide2;        //the JScrollPane for the second side
    private JPanel pnlSide2;                //the JPanel for all the JTextFields of the second side
    private JList lstCards;                 //list with all cards, which already have been added to the stack
    private JButton btnAddCard;             //Button to add Card (InformationGroup)
    private Stack stack;                    //the stack itself, which is being filled with InformationGroups
    private Imprend imprend;

    private ResourceBundle resource;        //ResourceBunlde with all Strings occuring in the GUI
    private int tFieldLength = 25;          //The lenght of one JTextFields of the two sides
    private int cursPos = 0;                //The "position" of the cursor. It says which field/button is comming next when NextAction is being called. The order is: TextField for 1st side (0),
                                            //TextField for 2nd side (1), Button to add card is being pressed. Then the whole thing restarts. The value is saying at which point the cursor is at the moment
    /**
     * Konstruktor, der alle Elemente des Panels initialisert und diese anordnet.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     * @param stack  Stapel, der erstellt oder ergänzt werden soll.
     */
    public JAddPanel(final Imprend imprend, final Stack stack) {
        resource = imprend.settings.getResourceBundle();

        this.stack = stack;
        this.imprend = imprend;

        JPanel pnlTop = new JPanel();
        JLabel lblStackName = new JLabel(resource.getString("stackName")+": ");
        JLabel lblNameOfStack = new JLabel(stack.getName());
        final JComboBox<String> combo = new JComboBox<>();

        JPanel pnlCenter = new JPanel();
        btnAddCard = new JButton(resource.getString("addCard"));
        JButton btnAddStack = new JButton(resource.getString("addStack"));
        lstCards = new JList();
        JPanel pnlRightSide = new JPanel();
        lstModel = new DefaultListModel<>();
        final JScrollPane scrollPane = new JScrollPane();
        lstCards.setFixedCellWidth(imprend.getFrame().getWidth()/10);

        combo.addItem(resource.getString("TypeCard"));
        combo.addItem(resource.getString("TypeBothCard"));

        lstCards.setModel(lstModel);
        scrollPane.setViewportView(lstCards);

        final JPanel pnlInfoGroup = new JPanel();
        JLabel lblComment = new JLabel(resource.getString("comment"));
        final JTextField tFieldComment = new JTextField(50);
        final JLabel lblSide1 = new JLabel(resource.getString("question"));
        btnAddSyn = new JButton(resource.getString("addSynonyms"));
        side1 = new ArrayList<>();
        scrlPnSide1 = new JScrollPane();
        pnlSide1 = new JPanel();
        final JLabel lblSide2 = new JLabel(resource.getString("answer"));
        btnAddSynToAnw = new JButton(resource.getString("addSynonyms"));
        side2 = new ArrayList<>();
        scrlPnSide2 = new JScrollPane();
        pnlSide2 = new JPanel();

        scrlPnSide1.setViewportView(pnlSide1);
        side1.add(new JTextField(tFieldLength));
        pnlSide1.add(side1.get(0));

        scrlPnSide2.setViewportView(pnlSide2);
        side2.add(new JTextField(tFieldLength));
        pnlSide2.add(side2.get(0));

        //set size of scrollPanes. Height is absolute, because I don't know how the get the height of a label, or TextField
        scrlPnSide1.setPreferredSize(new Dimension(imprend.getFrame().getWidth()/3, 50));
        scrlPnSide2.setPreferredSize(new Dimension(imprend.getFrame().getWidth()/3, 50));

        pnlTop.add(lblStackName);
        pnlTop.add(lblNameOfStack);
        pnlTop.add(combo);

        //Fonts
        lblStackName.setFont(imprend.settings.getTitleFont());
        lblNameOfStack.setFont(imprend.settings.getTitleFont());

        //Layout
        pnlInfoGroup.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 0, 0, 5);
        pnlInfoGroup.add(lblSide1, c);

        c.gridx = 1;
        c.gridy = 1;
        pnlInfoGroup.add(scrlPnSide1 , c);

        c.gridx = 2;
        c.gridy = 1;
        pnlInfoGroup.add(btnAddSyn, c);

        c.gridx = 0;
        c.gridy = 2;
        pnlInfoGroup.add(lblSide2, c);

        c.gridx = 1;
        c.gridy = 2;
        pnlInfoGroup.add(scrlPnSide2, c);

        c.gridx = 2;
        c.gridy = 2;
        pnlInfoGroup.add(btnAddSynToAnw, c);

        c.gridx = 0;
        c.gridy = 3;
        pnlInfoGroup.add(lblComment, c);

        c.gridx = 1;
        c.gridy = 3;;
        pnlInfoGroup.add(tFieldComment, c);

        c.gridx = 0;
        c.gridy = 4;
        pnlInfoGroup.add(btnAddCard, c);

        c.gridx = 1;
        c.gridy = 4;
        pnlInfoGroup.add(btnAddStack, c);

        pnlRightSide.add(scrollPane);

        pnlCenter.setLayout(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        pnlCenter.add(pnlInfoGroup);

        c.gridx = 1;
        c.gridy = 0;
        pnlCenter.add(pnlRightSide);

        setLayout(new BorderLayout());
        add(pnlTop, BorderLayout.PAGE_START);
        add(pnlCenter, BorderLayout.CENTER);

        //ActionListeners
        final ActionListener goCombo = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switch (combo.getSelectedIndex()) {
                    case 0:
                        //TypeCard
                        lblSide1.setText(resource.getString("question"));
                        lblSide2.setText(resource.getString("answer"));
                        break;
                    case 1:
                        //TypeBothCard
                        lblSide1.setText(resource.getString("Info1"));
                        lblSide2.setText(resource.getString("Info2"));
                }
            }
        };

        ActionListener goAddCard = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                InformationGroup infoGroup = new InformationGroup();
                //add side1 (this can be a Question or an Information
                for(int i = 0; i < side1.size(); i++) {
                    //if the text in the textfield is empty, no Object will be generated, because there would be no meaningfull information in it.
                    if(!side1.get(i).getText().equals("")) {
                        switch (combo.getSelectedIndex()) {
                            case 0:
                                Question question = new Question();
                                question.setInformation(side1.get(i).getText());
                                question.setGroup("1");
                                infoGroup.addInformation(question);
                                break;
                            case 1:
                                Information info2 = new Information();
                                info2.setInformation(side1.get(i).getText());
                                info2.setGroup("1");
                                infoGroup.addInformation(info2);
                                break;
                        }
                    }
                }

                //Add side2 (which always is an Information-Object)
                for(int i = 0; i < side2.size(); i++) {
                    //if the text in the textfield is empty, no Object will be generated, because there would be no meaningfull information in it.
                    if(!side2.get(i).getText().equals("")) {
                        Information info = new Information();
                        info.setInformation(side2.get(i).getText());
                        info.setGroup("2");
                        infoGroup.addInformation(info);
                    }
                }

                infoGroup.setComment(tFieldComment.getText());
                if(infoGroup.getAmountInformations() > 0) {
                    stack.addInformationGroup(infoGroup);
                }
                //clear side1
                for(int i = side1.size()-1; i > 0 ; i--) {
                    pnlSide1.remove(side1.get(i));
                    side1.remove(i);
                }
                side1.get(0).setText("");

                //clear side2
                for(int i = side2.size()-1; i > 0 ; i--) {
                    pnlSide2.remove(side2.get(i));
                    side2.remove(i);
                }
                side2.get(0).setText("");
                //clear comment
                tFieldComment.setText("");

                //need to do this because elsewise the newly added elements won't show up. Another way would be to resize the window (make it a little bigger and then shrink it to the old size). I don't know
                //why this is so. Bug in Java?
                imprend.switchPanel(imprend.strPnlMenu);
                imprend.switchPanel(imprend.strPnlAdd);

                //the created Card does not need to be added to the lstCards, because it will be done by the Methode reloadCardsList() which is called by the overwritten setVisible() methode, which is called
                //because I switched the panels (see above)
                //sets Cursor so the user can start to type directly
                side1.get(0).requestFocusInWindow();
            }
        };

        ActionListener goAddStack = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!side1.get(0).getText().equals("") && !side2.get(0).getText().equals("")) {
                    btnAddCard.doClick();
                }
                Save.saveStack(stack);
                imprend.getPnlMenu().reloadStackList(imprend);
                imprend.switchPanel(imprend.strPnlMenu);
            }
        };

        ActionListener goAddSyn = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                side1.add(new JTextField(tFieldLength));
                pnlSide1.add(side1.get(side1.size() - 1));
                //need to do this because elsewise the newly added elements won't show up. Another way would be to resize the window (make it a little bigger and then shrink it to the old size). I don't know
                //why this is so. Bug in Java?
                imprend.switchPanel(imprend.strPnlMenu);
                imprend.switchPanel(imprend.strPnlAdd);
                side1.get(side1.size()-1).requestFocusInWindow();
            }
        };

        ActionListener goAddSynToAns = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                side2.add(new JTextField(tFieldLength));
                pnlSide2.add(side2.get(side2.size() - 1));
                //need to do this because elsewise the newly added elements won't show up. Another way would be to resize the window (make it a little bigger and then shrink it to the old size). I don't know
                //why this is so. Bug in Java?
                imprend.switchPanel(imprend.strPnlMenu);
                imprend.switchPanel(imprend.strPnlAdd);
                side2.get(side2.size()-1).requestFocusInWindow();
            }
        };

        combo.addActionListener(goCombo);
        btnAddCard.addActionListener(goAddCard);
        btnAddStack.addActionListener(goAddStack);
        btnAddSyn.addActionListener(goAddSyn);
        btnAddSynToAnw.addActionListener(goAddSynToAns);
        lstCards.addMouseListener(this);
        loadKeyBindings(imprend);
    }

    private void reloadCardsList() {
        lstModel.clear();
        for(int i = 0; i < stack.getAmountInformationGroups(); i++) {
            addInformationGroupToList(stack.getInfoGroupById(i));
        }
    }

    /**
     * Für genaue Bespreibung siehe {@link JNavPanel#cleanUp(Imprend)}.
     * Dieses Panel macht nichts selber, es gibt true zurück
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     * @return true
     */
    @Override
    public boolean back(Imprend imprend) {
        return true;
    }

    /**
     * Für genaue Bespreibung siehe {@link JNavPanel#cleanUp(Imprend)}.
     * Speichert den Stapel
     * @param imprend   Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    @Override
    public void cleanUp(Imprend imprend) {
        Save.saveStack(stack);
    }

    /**
     * Implementiert den MouseListener. Diese Methode wird aufgerufen, wenn eine Maustaste geklickt wurde. Dient dazu einen Doppelklick auf die Liste mit
     * allen bisherigen Karten zu erkennen, damit die ausgewählte Karte bearbeitet werden kann.
     * Dabei wird automatisch zum entsprechendne Panel ({@link JEditPanel}) gewechselt und die gewählte Karte (bzw. InformationGroup) ausgewählt.
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() >= 2 && mouseEvent.getButton() == 1) {
            //When doubleclick on left mousebutton
            imprend.setPnlEdit(new JEditPanel(imprend, stack));
            imprend.addPanelToMain(imprend.getPnlEdit(), imprend.strPnlEdit);
            imprend.getPnlEdit().setSelection(lstCards.getSelectedIndex(), 0);
            imprend.switchPanel(imprend.strPnlEdit);

        }
    }

    private void addInformationGroupToList(InformationGroup infoGroup) {
        if(infoGroup.getAmountInformations() == 1) {
            lstModel.addElement(infoGroup.getInfoObjectById(0).getInformation());
        } else {
            lstModel.addElement(infoGroup.getInfoObjectById(0).getInformation()+"|"+infoGroup.getInfoObjectById(1).getInformation());
        }
    }

    /**
     * Implementiert den MouseListener. Diese Methde macht nichts.
     * @param mouseEvent
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    /**
     * Implementiert den MouseListener. Diese Methde macht nichts.
     * @param mouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    /**
     * Implementiert den MouseListener. Diese Methde macht nichts.
     * @param mouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    /**
     * Implementiert den MouseListener. Diese Methde macht nichts.
     * @param mouseEvent
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    /**
     * Überschreibt eine Methode des JPanels. Diese wird jedes Mal aufgerufen, wenn das Panel sichtbar gemacht wird.
     * Dient dazu, dass der Focus (also der Mauscursor) direkt auf das erste Textfeld (dasjenige für die Vorderseite) gesetzt wird.
     * Dies ermöglicht den Benutzer direkt mit dem Schreiben der neuen Karte anzufangen. Auch wird jedes mal die Liste mit den bisherigen Karten
     * neu geladen, damit diese immer aktualisiert wird, wenn man z.B. eine Karte über das {@link JEditPanel} geändert hat.
     * @param value
     */
    @Override
    public void setVisible(boolean value) {
        super.setVisible(value);
        //load the cardsList on start
        reloadCardsList();
        //used to set the focus on the first JTextField so the user can start writing immediately, without the need to click first into the JTextField
        side1.get(0).requestFocusInWindow();
    }
    /**
     * Lädt die Tastaturbelegung für das Panel.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    public void loadKeyBindings(Imprend imprend) {
        Action actionNext = new NextAction();
        int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap imap = getInputMap(mapName);
        KeyStroke nextKey = KeyStroke.getKeyStroke(imprend.settings.getKeyForNextAtJAddPanel());
        imap.put(nextKey, "next");
        ActionMap amap = getActionMap();
        amap.put("next", actionNext);
    }

    private class NextAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            switch (cursPos) {
                case 0:
                    side2.get(0).requestFocusInWindow();
                    cursPos++;
                    break;
                case 1:
                    btnAddCard.doClick();
                    side1.get(0).requestFocusInWindow();
                    cursPos = 0;
                    break;
            }
        }
    }
}
