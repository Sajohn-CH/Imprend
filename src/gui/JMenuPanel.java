package gui;

import informationManagement.InformationGroup;
import informationManagement.Stack;
import questionMethods.QMethCards;
import questionMethods.QuestionMethod;
import utilities.Imprend;
import utilities.Load;
import utilities.Save;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Kindklasse von {@link JNavPanel}. Stellt die Startseite/Hauptmenu des Progamms dar. Von hier aus, können Stapel gelernt, erstellt, editiert, gelöscht und erweitert
 * werden. <br>
 * Hier wird definiert, welches Panel mit welcher Abfragemethode gestartet wird. Der Benutzer kann dies über eine JComboBox auswählen.<br>
 * Erstellt am 30.06.15.
 *
 * @author Samuel Martin
 */
public class JMenuPanel extends JNavPanel {

    private DefaultListModel<String> lstModel;
    private ResourceBundle resource;

    /**
     * Konstruktor. Initialisert alle Elemente des Panels und ordnet diese an.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    public JMenuPanel(final Imprend imprend) {
        resource = imprend.settings.getResourceBundle();

        ImageIcon arrowHead = new ImageIcon("resources" + File.separator + "icons" + File.separator + "ArrowHead.png");
        ImageIcon plus = new ImageIcon("resources" + File.separator + "icons" + File.separator + "Plus.png");
        ImageIcon pen = new ImageIcon("resources" + File.separator + "icons" + File.separator + "Pen.png");

        JPanel pnlStacks = new JPanel();
        JPanel pnlControls = new JPanel();
        JButton btnStart = new JButton(arrowHead);
        JButton btnAdd = new JButton(plus);
        JButton btnEdit = new JButton(pen);
        JButton btnDeleteStack = new JButton(resource.getString("deleteStack"));
        JButton btnCopyStack = new JButton(resource.getString("copyStack"));
        final JComboBox<String> comboQMeth = new JComboBox<>();
        JScrollPane scrlPane = new JScrollPane();
        final JList<String> lstCards = new JList<>();
        lstModel = new DefaultListModel<>();

        //set tooltiptext to describe some gui-elements
        btnStart.setToolTipText(resource.getString("btnStart"));
        btnAdd.setToolTipText(resource.getString("btnAdd"));
        btnEdit.setToolTipText(resource.getString("btnEdit"));
        comboQMeth.setToolTipText(resource.getString("comboQMeths"));

        comboQMeth.addItem(resource.getString("QMethCards"));
        comboQMeth.addItem(resource.getString("QMethCardsWritten"));

        String[] stacks = Load.getAllObjectPathsIn(imprend.settings.getCardsDir());
        for (int i = 0; i < stacks.length; i++) {
            lstModel.addElement(stacks[i]);
        }


        lstCards.setModel(lstModel);
        scrlPane.setViewportView(lstCards);

        pnlControls.add(btnStart);
        pnlControls.add(btnAdd);
        pnlControls.add(btnEdit);
        pnlControls.add(btnCopyStack);
        pnlControls.add(btnDeleteStack);
        pnlControls.add(comboQMeth);

        pnlStacks.setLayout(new BorderLayout());
        pnlStacks.add(pnlControls, BorderLayout.PAGE_START);
        pnlStacks.add(scrlPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(pnlStacks, BorderLayout.LINE_START);

        //ActionListener
        ActionListener goStart = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                QuestionMethod questionMethod;
                if(lstCards.getSelectedValue() == null) {
                    //Error: No stack had been chosen
                    JOptionPane.showMessageDialog(imprend.getFrame(), resource.getString("MsgNoStackChoosen"), resource.getString("MsgNoStackChoosenShort"), JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (comboQMeth.getSelectedItem().equals(resource.getString("QMethCards"))) {
                    //QuestionMethod Cards
                    questionMethod = new QMethCards(lstCards.getSelectedValue());
                    //the Panels will handel it themselves when the stack has nothing to learn, they'll show the same thing, as when one finished learning
                    imprend.getPnlCard().initNewLearning(questionMethod);
                    imprend.switchPanel(imprend.strPnlCard);
                } else if(comboQMeth.getSelectedItem().equals(resource.getString("QMethCardsWritten"))) {
                    //Write words in QuestionMethod style
                    questionMethod = new QMethCards(lstCards.getSelectedValue());
                    //the Panels will handel it themselves when the stack has nothing to learn, they'll show the same thing, as when one finished learning
                    imprend.switchPanel(imprend.strPnlCardWritten);
                    imprend.getPnlCardWritten().initNewLearning(questionMethod);
                } else {
                    //add other QuestionMethod types here
                }
            }
        };

        ActionListener goAdd = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String stackName = "";
                int response = 0;
                if(lstCards.getSelectedIndex() != -1) {
                    String selectedStackName = new Stack(lstCards.getSelectedValue()).getName();
                    //a stack was selected while this button has been pressed -> Ask to create new Stack or to add InformationGroups to the selected stack
                    String[] options = {resource.getString("createNewStack"), resource.getString("toStack")+" "+selectedStackName+" "+ resource.getString("add"), resource.getString("Cancel")};
                    response = JOptionPane.showOptionDialog(imprend.getFrame(), resource.getString("MsgNewOrAddToStack"), resource.getString("MsgNewOrAddToStackShort"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if(response == 1) {
                        //add InformationGroups to existing stack
                       stackName = selectedStackName;

                    }
                }
                //if response == null, the user pressed cancel, when he was asked, if he would like to create a new stack, or add to an existing stack
                if(response != 2) {
                    if(stackName.equals("")) {
                        stackName = JOptionPane.showInputDialog(imprend.getFrame(), resource.getString("MsgStackName"));
                        if (stackName == null) {
                            //the given stackname is invalid
                            return;
                        }
                        if (stackName.equals("")) {
                            //the user wanted to create a stack with an emtpy name (stackname == null means he clicked cancel)
                            JOptionPane.showMessageDialog(imprend.getFrame(), resource.getString("MsgEmptyStackName"), resource.getString("MsgEmptyStackNameShort"), JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    stackName = imprend.settings.getCardsDir().getPath() + File.separator + stackName + ".xml";
                    Stack stack = new Stack(stackName);
                    imprend.setPnlAdd(new JAddPanel(imprend, stack));
                    imprend.addPanelToMain(imprend.getPnlAdd(), imprend.strPnlAdd);
                    imprend.switchPanel(imprend.strPnlAdd);
                }

            }
        };

        ActionListener goEdit = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String stackName = lstCards.getSelectedValue();
                if(stackName == null) {
                    //Error: No stack had been chossen
                    JOptionPane.showMessageDialog(imprend.getFrame(), resource.getString("MsgNoStackChoosenToEdit"), resource.getString("MsgNoStackChoosenShort"), JOptionPane.ERROR_MESSAGE);
                } else {
                    Stack stack = new Stack(stackName);
                    imprend.setPnlEdit(new JEditPanel(imprend, stack));
                    imprend.addPanelToMain(imprend.getPnlEdit(), imprend.strPnlEdit);
                    imprend.switchPanel(imprend.strPnlEdit);
                }
            }
        };

        ActionListener goDeleteStack = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String stackName = lstCards.getSelectedValue();
                if(stackName == null ){
                    //Error: No stack had been chossen
                    JOptionPane.showMessageDialog(null, resource.getString("MsgNoStackChoosenShort"), resource.getString("MsgNoStackChoosenShort"), JOptionPane.ERROR_MESSAGE);
                } else {
                    int answer = JOptionPane.showConfirmDialog(imprend.getFrame(), resource.getString("MsgSureDeleteStack"), resource.getString("MsgSure"), JOptionPane.OK_CANCEL_OPTION);
                    if(answer == 0) {
                        Stack stack =  new Stack(stackName);
                        File stackFile = stack.getStackFile();
                        stackFile.delete();
                    }
                    reloadStackList(imprend);
                }
            }
        };

        ActionListener goCopyStack = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String stackName = lstCards.getSelectedValue();
                if(stackName == null ){
                    //Error: No stack had been chossen
                    JOptionPane.showMessageDialog(imprend.getFrame(), resource.getString("MsgNoStackChoosenShort"), resource.getString("MsgNoStackChoosenShort"), JOptionPane.ERROR_MESSAGE);
                } else {
                    Stack stack =  new Stack(stackName);
                    //ask for new name;
                    String newName = JOptionPane.showInputDialog(imprend.getFrame(), resource.getString("MsgNewName"));
                    if(newName == null) {
                        //User clicked cancel
                        return;
                    }
                    //create new stack
                    File stackFile = stack.getStackFile();
                    String pathToStack = stackFile.getParentFile().getPath();
                    String fileType = stack.getStackFile().getPath().split("\\.")[1];
                    Stack stack2 = new Stack(pathToStack+File.separator+newName+"."+fileType);
                    //fill it with the content of the other stack
                    ArrayList<InformationGroup> infoGroups = (ArrayList<InformationGroup>) stack.copyAllInformationGroups();
                    for(int i = 0; i < infoGroups.size(); i++) {
                        stack2.addInformationGroup(infoGroups.get(i));
                    }
                    Save.saveStack(stack2);
                    reloadStackList(imprend);
                }
            }
        };

        btnStart.addActionListener(goStart);
        btnAdd.addActionListener(goAdd);
        btnEdit.addActionListener(goEdit);
        btnDeleteStack.addActionListener(goDeleteStack);
        btnCopyStack.addActionListener(goCopyStack);

    }

    /**
     * Details siehe {@link JNavPanel#back(Imprend)}. Gibt immer true zurück, da Panel nichts selber macht.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     * @return true
     */
    @Override
    public boolean back(Imprend imprend) {
        return true;
    }

    /**
     * Details siehe {@link JNavPanel#cleanUp(Imprend)}. Macht nichts, da es nichts zum speichern gibt.
     * @param imprend   Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    @Override
    public void cleanUp(Imprend imprend) {
    }

    /**
     * Lädt die Liste mit allen Stapeln neu.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    public void reloadStackList(Imprend imprend) {
        //reloads the list of all stacks.
        String[] stacks = Load.getAllObjectPathsIn(imprend.settings.getCardsDir());
        lstModel.removeAllElements();
        for (int i = 0; i < stacks.length; i++) {
            lstModel.addElement(stacks[i]);
        }

    }
}

