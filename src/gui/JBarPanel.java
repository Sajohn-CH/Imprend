package gui;

import utilities.Imprend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by samuel on 30.06.15.
 */
public class JBarPanel extends JPanel{

    public JBarPanel(final Imprend imprend) {
        JLabel lblTitle = new JLabel ("Imprend");

        ImageIcon arrow = new ImageIcon("resources" + File.separator + "icons" + File.separator +"BackArrow.png");
        ImageIcon home = new ImageIcon("resources" + File.separator + "icons" + File.separator +"Home.png");
        ImageIcon gear = new ImageIcon("resources" + File.separator + "icons" + File.separator +"Gear.png");


        JPanel pnlNav = new JPanel(); //Panel for navigation (Back, Home, Settings)
        JButton btnBack = new JButton(arrow);
        JButton btnHome = new JButton(home);
        JButton btnSettings = new JButton(gear);

        pnlNav.add(btnBack);
        pnlNav.add(btnHome);
        pnlNav.add(btnSettings);

        setLayout(new BorderLayout());
        add(lblTitle, BorderLayout.CENTER);
        add(pnlNav, BorderLayout.LINE_END);

        ActionListener goBack = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                imprend.back();
            }
        };

        ActionListener goHome = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                imprend.pnlCleanUp();
                imprend.switchPanel(imprend.strPnlMenu);
            }
        };

        ActionListener goSettings = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                imprend.switchPanel(imprend.strPnlSettings);

            }
        };

        //Fonts
        lblTitle.setFont(imprend.settings.getTitleFont());

        btnBack.addActionListener(goBack);
        btnHome.addActionListener(goHome);
        btnSettings.addActionListener(goSettings);
    }
}
