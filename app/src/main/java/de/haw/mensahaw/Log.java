package de.haw.mensahaw;

public class Log {
    public static void info(String string) {
        android.util.Log.d("LOL", string);
    }

    public static void error(String string) {android.util.Log.w("LOL", string);}
}
