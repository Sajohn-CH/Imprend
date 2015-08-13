package gui;

import informationManagement.Information;
import informationManagement.InformationGroup;
import informationManagement.Stack;
import questionMethods.QMethCards;
import questionMethods.QuestionMethod;
import utilities.Imprend;
import utilities.Load;
import utilities.Save;
import utilities.UTF8Control;

import javax.swing.*;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by samuel on 30.06.15.
 * Start"page" of the programm. From here the user can chose what to do. He can start learning and sees the statistics.
 */
public class JMenuPanel extends JNavPanel {

    private DefaultListModel<String> lstModel;

    public JMenuPanel(final Imprend imprend) {
        final ResourceBundle general = ResourceBundle.getBundle(imprend.settings.getResourceBundles()+".GeneralBundle", imprend.settings.getLocale(), new UTF8Control());
        final ResourceBundle menu = ResourceBundle.getBundle(imprend.settings.getResourceBundles()+".JMenuPanelBundle", imprend.settings.getLocale(), new UTF8Control());

        ImageIcon arrowHead = new ImageIcon("resources" + File.separator + "icons" + File.separator + "ArrowHead.png");
        ImageIcon plus = new ImageIcon("resources" + File.separator + "icons" + File.separator + "Plus.png");
        ImageIcon pen = new ImageIcon("resources" + File.separator + "icons" + File.separator + "Pen.png");


        JPanel pnlStacks = new JPanel();
        JPanel pnlControls = new JPanel();
        JButton btnStart = new JButton(arrowHead);
        JButton btnAdd = new JButton(plus);
        JButton btnEdit = new JButton(pen);
        JButton btnDeleteStack = new JButton(menu.getString("deleteStack"));
        JButton btnCopyStack = new JButton(menu.getString("copyStack"));
        final JComboBox<String> combo = new JComboBox<>();
        JScrollPane scrlPane = new JScrollPane();
        final JList<String> lstCards = new JList<>();
        lstModel = new DefaultListModel<>();

        combo.addItem(general.getString("QMethCards"));
        //combo.addItem(general.getString("QMethRepetition"));
        //combo.addItem(general.getString("QMethMockTest"));

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
        pnlControls.add(combo);

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
                    //Error: No stack had benn choosen
                    JOptionPane.showMessageDialog(imprend.frame, menu.getString("MsgNoStackChoosen"), menu.getString("MsgNoStackChoosenShort"), JOptionPane.ERROR_MESSAGE);
                    return;
                }else if (combo.getSelectedItem().equals(general.getString("QMethCards"))) {
                    //QuestionMethod Cards
                    questionMethod = new QMethCards(lstCards.getSelectedValue());
                    if(questionMethod.hasCards()) {
                        imprend.JCardPanel_initNewLearning(questionMethod);
                        imprend.switchPanel(imprend.strPnlCard);
                    } else {
                    }
                } else {
                    //add other QuestionMethod types here
                }
            }
        };

        ActionListener goAdd = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String stackName = JOptionPane.showInputDialog(imprend.frame, menu.getString("MsgStackName"));
                if (stackName == null){
                    //the given stackname is invalid
                    return;
                }
                if(stackName.equals("")) {
                    //the user wanted to create a stack with an emtpy name (stackname == null means he clicked cancel)
                    JOptionPane.showMessageDialog(imprend.frame, menu.getString("MsgEmptyStackName"), menu.getString("MsgEmptyStackNameShort"), JOptionPane.ERROR_MESSAGE);
                } else {
                    //I should later add here some other things to put the stack in the right folders
                    stackName = imprend.settings.getCardsDir().getPath() + File.separator + stackName + ".xml";
                    Stack stack = new Stack(stackName);
                    imprend.pnlAdd = new JAddPanel(imprend, stack);
                    imprend.addPanelToMain(imprend.pnlAdd, imprend.strPnlAdd);
                    imprend.switchPanel(imprend.strPnlAdd);
                    System.out.println("...");
                }
            }
        };

        ActionListener goEdit = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String stackName = lstCards.getSelectedValue();
                if(stackName == null) {
                    //Error: No stack had been chossen
                    JOptionPane.showMessageDialog(imprend.frame, menu.getString("MsgNoStackChoosenToEdit"), menu.getString("MsgNoStackChoosenShort"), JOptionPane.ERROR_MESSAGE);
                } else {
                    Stack stack = new Stack(stackName);
                    imprend.pnlEdit = new JEditPanel(imprend, stack);
                    imprend.addPanelToMain(imprend.pnlEdit, imprend.strPnlEdit);
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
                    JOptionPane.showMessageDialog(null, menu.getString("MsgNoStackChoosenShort"), menu.getString("MsgNoStackChoosenShort"), JOptionPane.ERROR_MESSAGE);
                } else {
                    int answer = JOptionPane.showConfirmDialog(imprend.frame, menu.getString("MsgSureDeleteStack"), menu.getString("MsgSure"), JOptionPane.OK_CANCEL_OPTION);
                    if(answer == 0) {
                        Stack stack =  new Stack(stackName);
                        File stackFile = stack.getStackFile();
                        //stackFile.delete();
                        stackFile.deleteOnExit();
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
                    JOptionPane.showMessageDialog(imprend.frame, menu.getString("MsgNoStackChoosenShort"), menu.getString("MsgNoStackChoosenShort"), JOptionPane.ERROR_MESSAGE);
                } else {
                    Stack stack =  new Stack(stackName);
                    //ask for new name;
                    String newName = JOptionPane.showInputDialog(imprend.frame, menu.getString("MsgNewName"));
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

    @Override
    public boolean back(Imprend imprend) {
        return true;
    }

    public void reloadStackList(Imprend imprend) {
        //reloads the list of all stacks.
        String[] stacks = Load.getAllObjectPathsIn(imprend.settings.getCardsDir());
        lstModel.removeAllElements();
        for (int i = 0; i < stacks.length; i++) {
            lstModel.addElement(stacks[i]);
        }

    }
}

