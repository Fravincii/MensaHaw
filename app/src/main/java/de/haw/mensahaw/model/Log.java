package de.haw.mensahaw.model;

public class Log {
    public static String info(String string) {
        android.util.Log.d("LOL", string);
        return string;
    }
    public static String error(String string) {
        android.util.Log.w("LOL", string);
        return string;
    }

}
