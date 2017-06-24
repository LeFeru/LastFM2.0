package be.ipl.rasli.lastfm20.Persistors;

import android.content.SharedPreferences;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rachid on 19/06/17.
 */
public class AndroidColorsPersistorsImpl implements AndroidColorsPersistors {

    private SharedPreferences sharedPreferences;
    private List<OnColorsDataChangeListener> listeners;

    private static final String KEY_COLOR_1 = "KEY_COLOR_1";
    private static final String KEY_COLOR_2 = "KEY_COLOR_2";
    public static final String KEY_PREF = "KEY_PREF_COLOR";

    public AndroidColorsPersistorsImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.listeners = new ArrayList<OnColorsDataChangeListener>();
    }


    @Override
    public void saveColor1(int color) {
        this.sharedPreferences.edit().putInt(KEY_COLOR_1, color).commit();
        notifyObservers();
    }

    @Override
    public void saveColor2(int color) {
        this.sharedPreferences.edit().putInt(KEY_COLOR_2, color).commit();
        notifyObservers();
    }

    @Override
    public int readColor1() {
        return this.sharedPreferences.getInt(KEY_COLOR_1, Color.GRAY);
    }

    @Override
    public int readColor2() {
        return this.sharedPreferences.getInt(KEY_COLOR_2, Color.GREEN);
    }

    @Override
    public void notifyObservers() {
        for(OnColorsDataChangeListener listener : this.listeners){
            listener.onColorsDataChange();
        }
    }

    @Override
    public void addObserver(OnColorsDataChangeListener observer) {
        this.listeners.add(observer);
    }
}
