package gui;

import utilities.Imprend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

/**
 * Kindklasse von JPanel. Beinhaltet alle Elemente der Leiste, die sich immer oben im Fenster befindet. Dieses Panel beinhaltet: <br>
 *     -Den Namen des Programms. <br>
 *     -Einen Knopf, um die letze Handlung rückgänig zu machen bzw. das letzte Panel anzuzeigen. (Icon: BackArrow.png) <br>
 *     -Einen Knopf, um zum Hauptmenü zu gelangen (Home-Knopf) (Icon: Home.png)<br>
 *     -Einen Knopf, um zu den Einstellungen zu gelangen. (Icon: Gear.png) <br>
 * Die Knöpfe werden durch Bilder dargestellt, welche im Ordner resources/icons zu finden sind. Es sind alles .png-Dateien. <br>
 * Erstellt am 30.06.15.
 * @author Samuel Martin
 */
public class JBarPanel extends JPanel{

    /**
     * Konstruktor. Initialisiert alle Element des Panels und ordnet diese an.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    public JBarPanel(final Imprend imprend) {
        ResourceBundle resource = imprend.settings.getResourceBundle();

        ImageIcon arrow = new ImageIcon("resources" + File.separator + "icons" + File.separator +"BackArrow.png");
        ImageIcon home = new ImageIcon("resources" + File.separator + "icons" + File.separator +"Home.png");
        ImageIcon gear = new ImageIcon("resources" + File.separator + "icons" + File.separator +"Gear.png");

        JLabel lblTitle = new JLabel ("Imprend");
        JPanel pnlNav = new JPanel(); //Panel for navigation (Back, Home, Settings)
        JButton btnBack = new JButton(arrow);
        JButton btnHome = new JButton(home);
        JButton btnSettings = new JButton(gear);

        //set tooltiptext to describe some gui-elements
        btnBack.setToolTipText(resource.getString("btnBack"));
        btnHome.setToolTipText(resource.getString("btnHome"));
        btnSettings.setToolTipText(resource.getString("btnSettings"));

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
