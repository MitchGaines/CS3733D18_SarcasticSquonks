package edu.wpi.cs3733d18.teamS.internationalization;

import java.util.Locale;
import java.util.ResourceBundle;

public class AllText {
    /**
     * Stores a Resource Bundle
     */
    private static ResourceBundle bundle = ResourceBundle.getBundle("all_text", new Locale("en"));

    /**
     * Stores a String for the Language abbreviation.
     */
    private static String language = "en";

    /**
     * Retrieves the language abbreviations.
     * @return The String array of the language abbreviations.
     */
    public static String[] getLanguages() {
        return new String[]{"en", "es", "ru"};
    }

    /**
     * Retrieves the key for the the particular object.
     * @param key the internationalization key for a particular object.
     * @return the language string wor that object.
     */
    public static String get(String key) {
        return bundle.getString(key);
    }

    /**
     * Retrieves the language.
     * @return the language.
     */
    public static String getLanguage() {
        return language;
    }

    /**
     * Retrieves the bundle.
     * @return the bundle.
     */
    public static ResourceBundle getBundle() {
        return bundle;
    }

    /**
     * Changes the language of the program.
     * @param localeStr the language to be changed to.
     */
    public static void changeLanguage(String localeStr) {
        language = localeStr;
        bundle = ResourceBundle.getBundle("all_text", new Locale(localeStr));
    }

}
