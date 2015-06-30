package utilities;

import java.io.File;

/**
 * Created by samuel on 30.06.15.
 * This class is mainly a collection of constants for the program.
 */

public class Settings {

    private File cardsDir;

    public Settings() {

    }

    public File getCardsDir() {
        return cardsDir;
    }

    public void setCardsDir(File cardsDir) {
        this.cardsDir = cardsDir;
    }
}
