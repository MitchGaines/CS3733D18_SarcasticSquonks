package internationalization;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AllText {
    /**
     * Stores a Resources Bundle
     */
    private static ResourceBundle bundle = ResourceBundle.getBundle("all_text", new Locale("en"));

    /**
     * Stores a String for the language
     */
    private static String language = "en";

    /**
     * Retrieves the Language.
     * @return The Language abbreviations.
     */
    public static String[] getLanguages() {
        return new String[] {"en", "es", "ru"};
    }

    /**
     * Gets the String key from the bundle.
     * @param key The identifier for the bundle key.
     * @return
     */
    public static String get(String key) {
        return bundle.getString(key);
    }

    /**
     * Retrieves the Language.
     * @return the Language.
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
     * Changes the language of the text displayed.
     * @param localeStr the local string.
     */
    public static void changeLanguage(String localeStr) {
        language = localeStr;
        bundle = ResourceBundle.getBundle("all_text", new Locale(localeStr));
    }

}
