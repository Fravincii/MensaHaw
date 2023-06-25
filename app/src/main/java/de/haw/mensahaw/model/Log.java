package de.haw.mensahaw.model;

public class Log {
    public static void info(String string) {
        android.util.Log.d("Info", string);
    }
    public static void error(String string) {
        android.util.Log.w("Error", string);
    }

}
