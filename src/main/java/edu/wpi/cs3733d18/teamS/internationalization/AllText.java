package edu.wpi.cs3733d18.teamS.internationalization;

import java.util.Locale;
import java.util.ResourceBundle;

public class AllText {
    private static ResourceBundle bundle = ResourceBundle.getBundle("all_text", new Locale("en"));
    private static String language = "en";
    public static String[] getLanguages() {
        return new String[] {"en", "es", "ru"};
    }
    public static String get(String key) {
        return bundle.getString(key);
    }

    public static String getLanguage() {
        return language;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static void changeLanguage(String localeStr) {
        language = localeStr;
        bundle = ResourceBundle.getBundle("all_text", new Locale(localeStr));
    }

}