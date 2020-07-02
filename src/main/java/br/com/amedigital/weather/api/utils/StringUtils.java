package br.com.amedigital.weather.api.utils;

import java.text.Normalizer;

public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static boolean isEmpty(String text) {
        if (text == null) return true;

        if (text.trim().isEmpty()) return true;

        return text.equalsIgnoreCase("null");
    }

    public static String removeAccents(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

}
