package gui;

import utilities.Imprend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Created by samuel on 03.07.15.
 */
public class JAddPanel extends JNavPanel{
    public JAddPanel(Imprend imprend) {
        final ResourceBundle addPanel = ResourceBundle.getBundle(imprend.settings.getResourceBundles()+".JAddPanelBundle");

        JPanel pnlTop = new JPanel();
        JLabel lblStackName = new JLabel(addPanel.getString("stackName"));
        JTextField tFieldStackName = new JTextField(20);
        JLabel lblType = new JLabel(addPanel.getString("Type"));
        final JComboBox<String> combo = new JComboBox<>();

        combo.addItem(addPanel.getString("TypeCard"));
        combo.addItem(addPanel.getString("TypeBothCard"));
        //combo.addItem(addPanel.getString("TypeManual"));

        JPanel pnlInfoGroup = new JPanel();
        JLabel lblComment = new JLabel(addPanel.getString("comment"));
        JTextField tFieldComment = new JTextField(50);
        final JLabel lblSide1 = new JLabel(addPanel.getString("question"));
        JTextField tFieldSide1 = new JTextField(30);
        final JLabel lblSide2 = new JLabel(addPanel.getString("answer"));
        JTextField tFieldSide2 = new JTextField(30);

        pnlTop.add(lblStackName);
        pnlTop.add(tFieldStackName);
        pnlTop.add(combo);

        pnlInfoGroup.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 1;
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

        setLayout(new BorderLayout());
        add(pnlTop, BorderLayout.PAGE_START);
        add(pnlInfoGroup, BorderLayout.CENTER);

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

        combo.addActionListener(goCombo);
    }

    @Override
    public void back(Imprend imprend) {

    }
}
