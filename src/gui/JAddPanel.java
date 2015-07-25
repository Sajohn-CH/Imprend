package gui;

import informationManagement.Information;
import informationManagement.InformationGroup;
import informationManagement.Question;
import informationManagement.Stack;
import utilities.Imprend;
import utilities.Save;
import utilities.UTF8Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Created by samuel on 03.07.15.
 */
public class JAddPanel extends JNavPanel{
    private int idCounter;      //used in the method getNextId(). DO NOT CHANGE THE VALUE!!

    public JAddPanel() {
        add(new JLabel("Hallo Test"));
    }

    public JAddPanel(final Imprend imprend, final Stack stack) {
        final ResourceBundle addPanel = ResourceBundle.getBundle(imprend.settings.getResourceBundles()+".JAddPanelBundle", new UTF8Control());
        idCounter = 0;

        JPanel pnlTop = new JPanel();
        JLabel lblStackName = new JLabel(addPanel.getString("stackName"));
        JLabel lblNameOfStack = new JLabel(stack.getName());
        //final JTextField tFieldStackName = new JTextField(20);
        JLabel lblType = new JLabel(addPanel.getString("Type"));
        final JComboBox<String> combo = new JComboBox<>();
        final JButton btnAddCard = new JButton(addPanel.getString("addCard"));
        JButton btnAddStack = new JButton(addPanel.getString("addStack"));
        JList listCards = new JList();
        JPanel pnlRightSide = new JPanel();
        DefaultListModel<String> lstModel = new DefaultListModel<>();
        JScrollPane scrollPane = new JScrollPane();

        combo.addItem(addPanel.getString("TypeCard"));
        combo.addItem(addPanel.getString("TypeBothCard"));
        //combo.addItem(addPanel.getString("TypeManual"));

        listCards.setModel(lstModel);
        scrollPane.setViewportView(listCards);

        JPanel pnlInfoGroup = new JPanel();
        JLabel lblComment = new JLabel(addPanel.getString("comment"));
        final JTextField tFieldComment = new JTextField(50);
        final JLabel lblSide1 = new JLabel(addPanel.getString("question"));
        final JTextField tFieldSide1 = new JTextField(30);
        final JLabel lblSide2 = new JLabel(addPanel.getString("answer"));
        final JTextField tFieldSide2 = new JTextField(30);

        pnlTop.add(lblStackName);
        //pnlTop.add(tFieldStackName);
        pnlTop.add(lblNameOfStack);
        pnlTop.add(combo);

        pnlInfoGroup.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 0, 0, 5);
        pnlInfoGroup.add(lblSide1, c);

        c.gridx = 1;
        c.gridy = 1;
        pnlInfoGroup.add(tFieldSide1, c);

        c.gridx = 0;
        c.gridy = 2;
        pnlInfoGroup.add(lblSide2, c);

        c.gridx = 1;
        c.gridy = 2;
        pnlInfoGroup.add(tFieldSide2, c);

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

        setLayout(new BorderLayout());
        add(pnlTop, BorderLayout.PAGE_START);
        add(pnlInfoGroup, BorderLayout.CENTER);
        add(pnlRightSide, BorderLayout.LINE_END);

        //ActionListeners
        final ActionListener goCombo = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switch (combo.getSelectedIndex()) {
                    case 0:
                        //TypeCard
                        lblSide1.setText(addPanel.getString("question"));
                        lblSide2.setText(addPanel.getString("answer"));
                        break;
                    case 1:
                        //TypeBothCard
                        lblSide1.setText(addPanel.getString("Info1"));
                        lblSide2.setText(addPanel.getString("Info2"));
                }
            }
        };

        ActionListener goAddCard = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                InformationGroup infoGroup = new InformationGroup();
                Information info1 = new Information();
                info1.setInformation(tFieldSide2.getText());
                info1.setId(getNextId());
                infoGroup.addInformation(info1);
                switch (combo.getSelectedIndex()) {
                    case 0:
                        Question question = new Question();
                        question.setInformation(tFieldSide1.getText());
                        question.setId(getNextId());
                        infoGroup.addQuestion(question);
                        break;
                    case 1:
                        Information info2 = new Information();
                        info2.setInformation(tFieldSide1.getText());
                        info2.setId(getNextId());
                        infoGroup.addInformation(info2);
                        break;
                }
                infoGroup.setComment(tFieldComment.getText());
                stack.addInformationGroup(infoGroup);
                tFieldSide1.setText("");
                tFieldSide2.setText("");
            }
        };

        ActionListener goAddStack = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!(tFieldSide1.equals("") && tFieldSide2.equals(""))) {
                    btnAddCard.doClick();
                }
                Save.saveStack(stack);
                imprend.JMenuPanel_reloadStackList(imprend);
                imprend.switchPanel(imprend.strPnlMenu);
            }
        };

        combo.addActionListener(goCombo);
        btnAddCard.addActionListener(goAddCard);
        btnAddStack.addActionListener(goAddStack);
    }

    @Override
    public void back(Imprend imprend) {
        imprend.switchPanel(imprend.strPnlMenu);
    }

    private int getNextId() {
        /*This method will return the next available id for an InfoObject for this stack;
         *The idCounter will count all ids. So the value the idCounter has, is always the same value as an new InfoObject in the stack would receive.
         *So everytime an InfoObject gets it's id by the idCounter, the idCounter needs to be increased by one.
         */
        idCounter++;
        return idCounter -1;
    }

}
