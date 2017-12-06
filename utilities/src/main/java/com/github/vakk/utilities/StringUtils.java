package com.github.vakk.utilities;

/**
 * Created by Valery Kotsulym on 10/25/2017.
 */

public class StringUtils {
    public static boolean isEqual(String first, String second) {
        if (first == null && second == null) {
            return true;
        } else if (first == null) {
            return false;
        } else if (second == null) {
            return false;
        } else return first.equals(second);
    }

    public static boolean isEmpty(String value) {
        return value != null && value.trim().length() == 0;
    }
}
