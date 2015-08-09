package gui;

import utilities.Imprend;
import utilities.UTF8Control;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created by samuel on 07.08.15.
 * Panel to create e card. It is not a child of JNavPanel because it is not thought to act as a panel alone, It should be integrated in another panel. (JAddPanel and JEditPanel).
 */
public class JAddCardPanel extends JPanel{

    public JAddCardPanel(Imprend imprend) {
        final ResourceBundle addPanel = ResourceBundle.getBundle(imprend.settings.getResourceBundles()+".JAddPanelBundle", new UTF8Control());

        JPanel pnlInfoGroup = new JPanel();
        JLabel lblComment = new JLabel(addPanel.getString("comment"));
        final JTextField tFieldComment = new JTextField(50);
        final JLabel lblSide1 = new JLabel(addPanel.getString("question"));
        final JTextField tFieldSide1 = new JTextField(30);
        final JLabel lblSide2 = new JLabel(addPanel.getString("answer"));
        final JTextField tFieldSide2 = new JTextField(30);
        final JButton btnAddCard = new JButton(addPanel.getString("addCard"));

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
    }
}
