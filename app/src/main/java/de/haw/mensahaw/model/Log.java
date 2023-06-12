package de.haw.mensahaw.model;

public class Log {
    public static void info(String string) {
        android.util.Log.d("LOL", string);
    }
    public static void info(String string, Float value) {
        android.util.Log.d("LOL", string + ", " + value.toString());
    }
    public static void error(String string) {android.util.Log.w("LOL", string);}
}
