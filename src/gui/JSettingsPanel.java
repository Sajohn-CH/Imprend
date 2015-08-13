
package gui;

import utilities.Imprend;
import utilities.UTF8Control;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by samuel on 30.06.15.
 */
public class JSettingsPanel extends JNavPanel {

    private JTextField tFieldCardPath;
    private JComboBox<String> comboLang;
    private JComboBox<Integer> comboFontSize;
    private JComboBox<String> comboFontName;
    private JLabel[] lblKeyResp;

    public JSettingsPanel(final Imprend imprend) {
        final ResourceBundle resource = ResourceBundle.getBundle(imprend.settings.getResourceBundles() + ".JSettingsPanelBundle", imprend.settings.getLocale(), new UTF8Control());
        ResourceBundle resourceJCardPanel = ResourceBundle.getBundle(imprend.settings.getResourceBundles() + ".JCardPanelBundle", imprend.settings.getLocale(), new UTF8Control());

        JLabel lblTitle = new JLabel(resource.getString("settings"));
        JLabel lblCardPath = new JLabel(resource.getString("cardPath"));
        tFieldCardPath = new JTextField(40);
        tFieldCardPath.setText(imprend.settings.getCardsDir().getAbsolutePath());
        JButton btnCardPath = new JButton(resource.getString("findDir"));
        JLabel lblLanguage = new JLabel(resource.getString("language")+"*");
        comboLang = new JComboBox<>();
        JLabel lblFontSize = new JLabel(resource.getString("fontSize"));
        comboFontSize = new JComboBox<>();
        JLabel lblFontName = new JLabel(resource.getString("fontName"));
        comboFontName = new JComboBox<>();
        final JLabel lblPreviewFont = new JLabel(imprend.settings.getFontName());

        JLabel lblKeyLayout = new JLabel(resource.getString("keyLayout"));
        JLabel lblForJCardPanel = new JLabel(resource.getString("forJCardPanel"));
        final String[] keys = imprend.settings.getKeysForJCardBtns();
        JLabel[] lblBtnResp = new JLabel[5];
        lblKeyResp = new JLabel[5];
        //Fill the,lables with all the Buttons in the JCardPanel
        for(int i = 0; i < lblBtnResp.length; i++) {
            lblBtnResp[i] = new JLabel(resource.getString("button") + " \"" + resourceJCardPanel.getString("Resp" + i) + "\"");
            //Add two spaces at begin and end to make the label bigger and easier to click on
            lblKeyResp[i] = new JLabel("  "+keys[i]+"  ");
        }

        lblLanguage.setToolTipText(resource.getString("requiresRestart"));

        //Fill the combo with all available languages
        for(int i = 0; i < imprend.settings.getAvailableLocales().size(); i++) {
            ResourceBundle avaLang = ResourceBundle.getBundle(imprend.settings.getResourceBundles() + ".GeneralBundle", imprend.settings.getAvailableLocales().get(i), new UTF8Control());
            comboLang.addItem(avaLang.getString("languageName"));
        }

        for(int i = 8; i < 24; i+=2) {
            comboFontSize.addItem(i);
        }
        comboFontSize.setSelectedItem(imprend.settings.getFontSize());

        //Fill the combo with all possible fonts
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fonts[] = g.getAvailableFontFamilyNames();
        for(int i = 0; i < fonts.length; i++) {
            comboFontName.addItem(fonts[i]);
        }
        comboFontName.setSelectedItem(imprend.settings.getFontName());


        //ActionListeners
        ActionListener goCardPath = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setSelectedFile(new File(tFieldCardPath.getText()));
                int returnVal = chooser.showOpenDialog(imprend.frame);
                if(returnVal == chooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    tFieldCardPath.setText(file.getAbsolutePath());
                }
            }
        };

        btnCardPath.addActionListener(goCardPath);

        //React on doubleclick on a label, to change the key
        for(int i = 0; i < lblKeyResp.length; i++) {
            final int finalI = i;
            lblKeyResp[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    //Only for doubleclilck with the left button
                    if(e.getButton() == 1 && e.getClickCount() == 2) {
                        String newKey = JOptionPane.showInputDialog(imprend.frame, resource.getString("pressKey"));
                        if (newKey.split("").length != 2) {
                           JOptionPane.showMessageDialog(imprend.frame, resource.getString("MsgOnlyOneKey"), resource.getString("MsgOnlyOneKey"), JOptionPane.ERROR_MESSAGE);
                        } else {
                            //Add two spaces at begin and end to make the label bigger and easier to click on
                            lblKeyResp[finalI].setText("  "+newKey+"  ");
                            keys[finalI] = newKey;
                        }

                    }
                }
            });
        }

        ItemListener goComboFontName = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                String font = itemEvent.getItem().toString();
                lblPreviewFont.setText(font);
                lblPreviewFont.setFont(new Font(font, Font.PLAIN, imprend.settings.getFontSize()));
            }
        };
        comboFontName.addItemListener(goComboFontName);

        //Fonts
        lblTitle.setFont(imprend.settings.getTitleFont());
        lblKeyLayout.setFont(imprend.settings.getSubtitleFont());
        lblForJCardPanel.setFont(imprend.settings.getSmallSubtitleFont());

        //Layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 5;
        c.insets = new Insets(5, 5, 0, 0);
        add(lblTitle, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        add(lblCardPath, c);

        c.gridx = 1;
        c.gridy = 2;
        add(tFieldCardPath, c);

        c.gridx = 2;
        c.gridy = 2;
        add(btnCardPath, c);

        c.gridx = 0;
        c.gridy = 3;
        add(lblLanguage, c);

        c.gridx = 1;
        c.gridy = 3;
        add(comboLang, c);

        c.gridx = 0;
        c.gridy = 4;
        add(lblFontSize, c);

        c.gridx = 1;
        c.gridy = 4;
        add(comboFontSize, c);

        c.gridx = 0;
        c.gridy = 5;
        add(lblFontName, c);

        c.gridx = 1;
        c.gridy = 5;
        add(comboFontName, c);

        c.gridx = 2;
        c.gridy = 5;
        add(lblPreviewFont, c);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 5;
        add(lblKeyLayout, c);

        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 5;
        add(lblForJCardPanel, c);

        c.gridwidth  = 1;
        for(int i = 0; i < lblBtnResp.length; i++) {
            c.gridx = 0;
            c.gridy = 8+i;
            add(lblBtnResp[i], c);

            c.gridx = 1;
            c.gridy = 8+i;
            add(lblKeyResp[i], c);
        }
    }

    @Override
    public boolean back(Imprend imprend) {
        cleanUp(imprend);
        return true;
    }

    @Override
    public void cleanUp(Imprend imprend) {
        //Save settings
        imprend.settings.setCardsDir(new File(tFieldCardPath.getText()));
        imprend.settings.setLocale(imprend.settings.getAvailableLocales().get(comboLang.getSelectedIndex()));
        imprend.settings.setFontSize((int)comboFontSize.getSelectedItem());
        imprend.settings.setFontName(String.valueOf(comboFontName.getSelectedItem()));
        String[] keys = imprend.settings.getKeysForJCardBtns();
        for(int i = 0; i < lblKeyResp.length; i++) {
            //use substring to sort out the spaces
            keys[i] = lblKeyResp[i].getText().substring(2, 3);
        }
        imprend.settings.setKeysForJCardBtns(keys);
        imprend.settings.saveSettings();
        imprend.loadFonts();
        imprend.loadKeyBindings();
    }
}
