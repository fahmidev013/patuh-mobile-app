package app.patuhmobile.utils;


import com.google.gson.Gson;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class StringUtils {
    public static boolean isStringNullOrEmpty(String str) {
        return (str == null || str.isEmpty());
    }
}
