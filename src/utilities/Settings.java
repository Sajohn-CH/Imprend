package utilities;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by samuel on 30.06.15.
 * This class is mainly a collection of constants for the program.
 */

public class Settings {

    private File cardsDir;
    private String ResourceBundles;
    private Locale locale;
    private Dimension resolution;
    private File settingsFile;
    private Properties properties;
    private String[] keysForJCardBtns;

    //Fonts
    int fontSize;
    String fontName;

    public Settings() {
        properties = new Properties();
        //default values
        ResourceBundles = "resources.language";
        locale = Locale.getDefault();
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        resolution = new Dimension(g.getDefaultScreenDevice().getDisplayMode().getWidth(), g.getDefaultScreenDevice().getDisplayMode().getHeight());
        settingsFile = new File("resources" + File.separator + "settings.properties");
        cardsDir = new File("resources/cards");

        fontSize = 14;
        fontName = "timesNewRoman";

        keysForJCardBtns = new String[6];
        keysForJCardBtns[0] = "1";
        keysForJCardBtns[1] = "2";
        keysForJCardBtns[2] = "3";
        keysForJCardBtns[3] = "4";
        keysForJCardBtns[4] = "5";
        keysForJCardBtns[5] = "ENTER";

        loadSettings();
    }

    public File getCardsDir() {
        return new File(properties.getProperty("cardsDir", "resources/cards"));
    }

    public void setCardsDir(File cardsDir) {
        this.cardsDir = cardsDir;
    }

    public String getResourceBundles() {
        return ResourceBundles;
    }

    public void setResourceBundles(String resourceBundles) {
        ResourceBundles = resourceBundles;
    }

    public Locale getLocale() {
        return new Locale(properties.getProperty("locale"));
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Dimension getResolution() {
        return resolution;
    }

    public void setResolution(Dimension resolution) {
        this.resolution = resolution;
    }

    public ArrayList<Locale> getAvailableLocales() {
        ArrayList<Locale> availableLocales = new ArrayList<>();
        try {
            FileInputStream inStream = new FileInputStream(settingsFile);
            properties.load(inStream);
            String[] avaLocales = properties.getProperty("availableLocales").split(",");
            availableLocales.clear();
            for (int i = 0; i < avaLocales.length; i++) {
                availableLocales.add(new Locale(avaLocales[i]));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return availableLocales;
    }

    public int getFontSize() {
        fontSize = Integer.valueOf(properties.getProperty("fontSize"));
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        properties.put("fontSize", String.valueOf(fontSize));
    }

    public String getFontName() {
        fontName = properties.getProperty("fontName");
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
        properties.put("fontName", fontName);
    }

    public Font getTitleFont() {
        //returns a Font for a title
        fontSize = getFontSize();
        fontName = getFontName();
        return new Font(fontName, Font.BOLD, fontSize+15);
    }

    public Font getTextFont() {
        //Font for a normal text
        fontSize = getFontSize();
        fontName = getFontName();
        return new Font(fontName, Font.PLAIN, fontSize);
    }

    public Font getSubtitleFont() {
        //returns a Font for a subttitle
        fontSize = getFontSize();
        fontName = getFontName();
        return new Font(fontName, Font.BOLD, fontSize+10);
    }

    public Font getSmallSubtitleFont() {
        //returns a Font for a small subttitle
        fontSize = getFontSize();
        fontName = getFontName();
        return new Font(fontName, Font.BOLD, fontSize+5);
    }

    public String[] getKeysForJCardBtns() {
        String str = "";
        for(int i = 0; i < keysForJCardBtns.length; i++) {
            str += keysForJCardBtns[i] + ", ";
        }
        keysForJCardBtns = properties.getProperty("keysForJCardBtns").split(",");
        return keysForJCardBtns;
    }

    public void setKeysForJCardBtns(String[] keysForJCardBtns) {
        this.keysForJCardBtns = keysForJCardBtns;
        String str = "";
        for(int i = 0; i < keysForJCardBtns.length; i++) {
            str += keysForJCardBtns[i] + ",";
        }
        properties.put("keysForJCardBtns", str);
        return;
    }

    public void saveSettings() {
        try {
            OutputStream outStream = new FileOutputStream(settingsFile);
            properties.store(outStream, "Here are the settings of imprend stored");
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSettings() {

        try {
            FileInputStream inStream = new FileInputStream(settingsFile);
            properties.load(inStream);
            inStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
