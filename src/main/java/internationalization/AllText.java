package internationalization;

import org.omg.IOP.CodecFactoryPackage.UnknownEncoding;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AllText {
    private static ResourceBundle bundle = ResourceBundle.getBundle("all_text", new Locale("en"));
    public static String get(String key) {
        try {
            return new String(bundle.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static void changeLanguage(String localeStr) {
        bundle = ResourceBundle.getBundle("all_text", new Locale(localeStr));
    }

}
