package sporedimkboot.utils;

public class Utils {
    public static String getUriReplacingChars(String uri){
        return uri.replaceAll(Constants.FILTER_NON_LETTERS_OR_NUMBERS, "_");
    }

}
