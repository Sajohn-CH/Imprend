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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by samuel on 30.06.15.
 * Start"page" of the programm. From here the user can chose what to do. He can start learning and sees the statistics.
 */
public class JMenuPanel extends JNavPanel {


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
        final JComboBox<String> combo = new JComboBox<>();
        JScrollPane scrlPane = new JScrollPane();
        final JList<String> lstCards = new JList<>();
        DefaultListModel<String> lstModel = new DefaultListModel<>();

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
                    System.out.println("noStack");
                    JOptionPane.showMessageDialog(null, menu.getString("MsgNoStackChoosen"), menu.getString("MsgNoStackChoosenShort"), JOptionPane.ERROR_MESSAGE);
                    return;
                }else if (combo.getSelectedItem().equals(general.getString("QMethCards"))) {
                    //QuestionMethod Cards
                    questionMethod = new QMethCards(lstCards.getSelectedValue());
                    if(questionMethod.hasCards()) {
                        imprend.JCardPanel_initNewLearning(questionMethod);
                        imprend.switchPanel(imprend.strPnlCard);
                    } else {
                        JOptionPane.showMessageDialog(null, menu.getString("MsgStackNoCardsToLearn"), menu.getString("MsgStackNoCardsToLearnShort"), JOptionPane.OK_OPTION);
                    }
                } else {
                    //add other QuestionMethod types here
                }
            }
        };

        ActionListener goAdd = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                imprend.switchPanel(imprend.strPnlAdd);
            }
        };

        ActionListener goEdit = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //This button will, instead of edit the stack, remove the learning progress. This is just there for testing purpose.
                //So a stack can be "learned" several time to test the program.
                Stack stack = new Stack(lstCards.getSelectedValue());
                ArrayList<InformationGroup> informationGroups = stack.getAllInfoGroups();
                for (int i = 0; i < informationGroups.size(); i++) {
                    ArrayList<Information> infos = informationGroups.get(i).getInformations();
                    for(int j = 0; j < infos.size(); j++) {
                        infos.get(j).setDate(new Date(0));
                        infos.get(j).setEase(2.5);
                        infos.get(j).setAmountRepetition(0);
                        infos.get(j).setOldDate(new Date(0));
                    }
                }
                Save.saveStack(stack);
            }
        };

        btnStart.addActionListener(goStart);
        btnAdd.addActionListener(goAdd);
        btnEdit.addActionListener(goEdit);

    }

    @Override
    public void back(Imprend imprend) {
        //more back is impossible :)
    }

}

