package br.com.amedigital.weather.api.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Util {

    public static String removeAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");//Somente caracteres UNicode
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static String removeSpace(String str) {
        return str.replaceAll("\\s+"," ");
    }
}
