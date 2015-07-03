package utilities;

import java.awt.*;
import java.io.File;
import java.util.Locale;
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

    public Settings() {
        //default values
        ResourceBundles = "resources.language";
        locale = Locale.getDefault();
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        resolution = new Dimension(g.getDefaultScreenDevice().getDisplayMode().getWidth(), g.getDefaultScreenDevice().getDisplayMode().getHeight());
    }

    public File getCardsDir() {
        return cardsDir;
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
        return locale;
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
}
