package com.balaji.notehomelane.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by balaji on 16/01/18.
 */

public class Validator {

    private static String TITLE_PATTERN = "^[A-Za-z0-9]{3,}$";
    private static String TEXT_PATTERN = "^[A-Za-z0-9]+(?:\\s[a-z]+)+$";

    public static boolean validateNoteTitle(String noteTitle) {
        Pattern pattern = Pattern.compile(TITLE_PATTERN);
        Matcher matcher = pattern.matcher(noteTitle);
        return matcher.matches();
    }

    public static boolean validateNoteText(String noteText) {
        Pattern pattern = Pattern.compile(TEXT_PATTERN);
        Matcher matcher = pattern.matcher(noteText);
        return matcher.matches();
    }
}
