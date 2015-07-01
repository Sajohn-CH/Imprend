package gui;

import InformationManagement.Question;
import QuestionMethods.QMethCards;
import QuestionMethods.QuestionMethod;
import utilities.Imprend;
import utilities.Load;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by samuel on 30.06.15.
 * Start"page" of the programm. From here the user can chose what to do. He can start learning and sees the statistics.
 */
public class JMenuPanel extends JNavPanel {


    public JMenuPanel(final Imprend imprend) {
        Locale currentLocale = Locale.getDefault();
        ResourceBundle general = ResourceBundle.getBundle("resources.language.GeneralBundle", currentLocale);

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

        combo.addItem("Karten");
        combo.addItem("Mocktest");
        combo.addItem("Repetition");

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
                if(combo.getSelectedItem().equals("Karten")) {
                    questionMethod = new QMethCards(lstCards.getSelectedValue());
                    imprend.JCardPanel_initNewLearning(questionMethod);
                } else {
                    //add other QuestionMethod types here
                }
                imprend.switchPanel(imprend.strPnlCard);
            }
        };

        ActionListener goAdd = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        };

        ActionListener goEdit = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

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

