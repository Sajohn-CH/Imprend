package utilities;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Klasse, die die Einstellungen des Programms verwaltet. Die Einstellungen werden in einer .properties-Datei gespeichert, die sich im Ordner resources befindet, der
 * mit dem Programm mitgeliefert wird.
 * Folgende Einstellungen werden hier verwaltet: <br>
 * -Ordner aller Stapel <br>
 * -ResourceBundle mit allen Strings für die GUI <br>
 * -die akutelle Sprache der GUI (locale) <br>
 * -alle verfügbaren Sprachen <br>
 * -Schriftfonten für Titel, Text, Untertitel etc. <br>
 * -Schriftart und Grösse, welche verwendet werden <br>
 * -Tastaturbelegung, für gewisse Panels <br>
 * Erstellt am 30.06.15
 *
 * @author Samuel Martin
 */
public class Settings {

    private Properties properties;
    private File settingsFile;

    private String PATH_RESOURCE_BUNDLES = "resources.language";

    /**
     * Konstruktor. Lädt alle Einstellungen aus der gegebenen Datei. Falls diese nicht vorhanden ist, werden die Einstellungen mit Standardwerten geladen und in der
     * Datei abgespeichert.
     * @param settingsFile  Einstellungsdatei
     */
    public Settings(File settingsFile) {
        properties = new Properties(getDefaultProperties());
        this.settingsFile = settingsFile;
        //define / calculate some values
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //create Settings-file with default values, if the file doesn't exists (e.g. on the very first start of an programm)
        if(!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            properties = getDefaultProperties();
            saveSettings();
        }
        //load the settings out of the file.
        loadSettings();
    }

    //returns a Properties with default values.
    private Properties getDefaultProperties() {
        Properties defProp = new Properties();
        defProp.setProperty("cardsDir", "resources"+File.separator+"cards");
        defProp.setProperty("locale", Locale.getDefault().toLanguageTag());
        defProp.setProperty("fontSize", "14");
        defProp.setProperty("fontName", "SansSerif");
        defProp.setProperty("keysForJCardBtns", "1,2,3,4,5,ENTER");
        defProp.setProperty("keyForNextAtJAddPanel", "ENTER");
        return defProp;
    }

    /**
     * Gibt Ordner, in dem alle Stapeln gespeichert sind, zurück
     * @return Ordner mit allen Stapeln
     */
    public File getCardsDir() {
        return new File(properties.getProperty("cardsDir"));
    }

    /**
     * Setzt den Ordner, in dem alle Stapeln gespeichert sind
     * @param cardsDir  neuer Ordner
     */
    public void setCardsDir(File cardsDir) {
        properties.setProperty("cardsDir", cardsDir.getAbsolutePath());
    }

    /**
     * Gibt relativen Pfad zum Ordner zurück, in dem alle ResourceBundles (.properties) liegen.
     * @return Pfad zu Sprachdateien
     */
    public String getResourceBundlesPath() {
        return PATH_RESOURCE_BUNDLES;
    }

    /**
     *
     * Überlädt die Methode {@link Settings#getResourceBundle(Locale)}. Als Locale wird der Rückgabewert der Methode {@link Settings#getLocale()} eingesetzt.
     * @return ResourceBundle, welches mit der aktuell gewählten Sprache gebraucht wird.
     */
    public ResourceBundle getResourceBundle() {
        return getResourceBundle(getLocale());
    }

    /**
     * Liefert das ResourceBundle des Programs mit der gegebenen Sprache (Locale) zurück. Im ResourceBundle sind alle Strings (also Text), die in der GUI vorkommen gepseichert. Dies macht es einfach eine neuen Sprache hinzuzufügen.
     * @param locale  Sprache (bzw. Locale) in der das ResourceBundle sein soll.
     * @return ResourceBundle in der gewünschten Sprache.
     */
    public ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(getResourceBundlesPath()+".MyResourceBundle", locale, new UTF8Control());
    }

    /**
     * Gibt das Locale-Objekt zurück, welches definiert, welche Sprache gebraucht wird (u.a. bei den JOptionPanes).
     * Wenn es dies nicht aus der Einstellungs-Datei auslesen kann, nimmt es die Standardsprache der JVM.
     * @return Locale-Objeckt
     */
    public Locale getLocale() {
        //generate a Locale out of the String in the .properties-files. There it is saved with the instruction locale.toLanguageTag().
        String[] strLocale = properties.getProperty("locale").split("-");
        switch (strLocale.length) {
            case 1:
                return new Locale(strLocale[0]);
            case 2:
                return new Locale(strLocale[0], strLocale[1]);
            case 3:
                return new Locale(strLocale[0], strLocale[1], strLocale[2]);
        }
        return Locale.getDefault();
    }

    /**
     * Setzt das Locale-Objekt. Dieses definiert, welche Sprache gebracuht wird (u.a. bei den JOptionPanes)
     * @param locale  Neues Locale-Objeckt
     */
    public void setLocale(Locale locale) {
       properties.setProperty("locale", locale.toLanguageTag());
    }

    /**
     * Gibt die verfügbaren Locale-Objekte, also Sprachen, zurück
     * @return verfügbare Sprachen
     */
    public ArrayList<Locale> getAvailableLocales() {
        ArrayList<Locale> availableLocales = new ArrayList<>();
        //returns all locales installed on the system
        Locale[] locales = Locale.getAvailableLocales();
        for(int i = 0; i < locales.length; i++) {
            //load to every locale an ResourceBundle. If the ResourceBundle does not exist it will take the default one (MyResourceBundle.properties)
            ResourceBundle bundle = ResourceBundle.getBundle("resources.language.MyResourceBundle", locales[i], new UTF8Control());
            //if the locale of the ResourceBundle is the same as the one it was initialised with, the ResourceBundle exists. bundle.getLocale() returns the locale of the
            //ResourceBundle. If the locale changes if the ResourceBundle does not exist.
            if(bundle.getLocale() == locales[i]) {
                availableLocales.add(locales[i]);
            }
        }
        //Checks if the locale of the default ResourceBunlde (The one without a locale added to it. This one is german) is in the available locales.
        if(!availableLocales.contains(Locale.GERMAN)) {
            //if not it adds it to the avaiable locales. This is needed, so in the settings, german will be displayed, eventhough there is no MyResourceBundle_de.properties, which would be unnecessary
            //because the defautl MyResourceBundle.properties is in german.
            availableLocales.add(Locale.GERMAN);
        }
        return availableLocales;
    }

    /**
     * Gibt die allgemeine Schriftgrösse zurück
     * @return Schriftgrösse
     */
    public int getFontSize() {
        return Integer.valueOf(properties.getProperty("fontSize"));
    }

    /**
     * Setzt die allgemeine Schriftgrösse.
     * @param fontSize  Neue Schriftgrösse
     */
    public void setFontSize(int fontSize) {
        properties.setProperty("fontSize", String.valueOf(fontSize));
    }

    /**
     * Gibt die Schriftart zurück.
     * @return Schriftart.
     */
    public String getFontName() {
        return properties.getProperty("fontName");
    }

    /**
     * Setzt eine neue Schriftart.
     * @param fontName  Neue Schriftart
     */
    public void setFontName(String fontName) {
        properties.setProperty("fontName", fontName);
    }

    /**
     * Gibt die SchriftFont zurück, die bei einem Titel gebraucht wird.
     * @return Titelfont
     */
    public Font getTitleFont() {
        //returns a Font for a title
        int fontSize = getFontSize();
        String fontName = getFontName();
        return new Font(fontName, Font.BOLD, fontSize+15);
    }

    /**
     * Gibt die SchriftFont zurück, die bei einem normalen Text gebraucht wird.
     * @return Textfont
     */
    public Font getTextFont() {
        //Font for a normal text
        int fontSize = getFontSize();
        String fontName = getFontName();
        return new Font(fontName, Font.PLAIN, fontSize);
    }

    /**
     * Gibt die SchriftFont zurück, die bei einem Untertitel gebraucht wird.
     * @return Utertitelfont
     */
    public Font getSubtitleFont() {
        //returns a Font for a subttitle
        int fontSize = getFontSize();
        String fontName = getFontName();
        return new Font(fontName, Font.BOLD, fontSize+10);
    }

    /**
     *  Gibt die SchriftFont zurück, die bei einem kleinem Untertitel gebraucht wird.
     * @return kleine Untertitelfont
     */
    public Font getSmallSubtitleFont() {
        //returns a Font for a small subttitle
        int fontSize = getFontSize();
        String fontName = getFontName();
        return new Font(fontName, Font.BOLD, fontSize+5);
    }

    /**
     * Gibt die Tastenbelegung für das {@link gui.JCardPanel} zurück. Dies geschieht mit einem Array aus Strings in dem alle Tasten stehen. Diese müssen noch via KeyStroke.getKeyStroke(Char keyChar)
     * umgewandelt werden.
     * @return Tastaturbelegung
     */
    public String[] getKeysForJCardBtns() {
        String[] keysForJCardBtns = properties.getProperty("keysForJCardBtns").split(",");
        return keysForJCardBtns;
    }

    /**
     * Setzt die Tastenbelegung für das {@link gui.JCardPanel}. Dies geschieht mit einem Array aus Strings in dem alle Tasten stehen. Diese müssen noch via KeyStroke.getKeyStroke(Char keyChar)
     * umgewandelt werden.
     * @param keysForJCardBtns Neue Tastaturbelegung
     */
    public void setKeysForJCardBtns(String[] keysForJCardBtns) {
        String str = "";
        for(int i = 0; i < keysForJCardBtns.length; i++) {
            str += keysForJCardBtns[i] + ",";
        }
        properties.setProperty("keysForJCardBtns", str);
    }

    /**
     * Gibt die Taste für das nächste Feld beim {@link gui.JAddPanel} zurück. Dies geschieht mit einem String in dem die Taste stehen. Diese müssen noch via KeyStroke.getKeyStroke(Char keyChar)
     * umgewandelt werden.
     * @return Taste
     */
    public String getKeyForNextAtJAddPanel() {
        return properties.getProperty("keyForNextAtJAddPanel");
    }

    /**
     * Setzt die Taste für das nächste Feld beim {@link gui.JAddPanel}. Dies geschieht mit einem String in dem die Taste stehen. Diese müssen noch via KeyStroke.getKeyStroke(Char keyChar)
     * umgewandelt werden.
     * @param keyForNextAtJAddPanel Neue Taste
     */
    public void setKeyForNextAtJAddPanel(String keyForNextAtJAddPanel) {
        properties.setProperty("keyForNextAtJAddPanel", keyForNextAtJAddPanel);
    }

    /**
     * Speichert die Einstellungen in einer .properties-Datei
     */
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

    /**
     * Lädt die Einstellungen aus einer .properties-Datei.
     */
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
