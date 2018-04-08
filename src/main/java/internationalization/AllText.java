package internationalization;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AllText {
    private static ResourceBundle bundle = ResourceBundle.getBundle("all_text", new Locale("en"));
    private static String language = "en";
    public static String[] getLanguages() {
        return new String[] {"en", "es", "ru"};
    }
    public static String get(String key) {
        try {
            return new String(bundle.getString(key).getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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
