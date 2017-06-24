package be.ipl.rasli.lastfm20.Persistors;

import android.content.SharedPreferences;

import be.ipl.rasli.lastfm20.DTOs.Artist;

/**
 * Created by rachid on 19/06/17.
 */
public class AndroidArtistesPreferesPersistorImpl implements AndroidArtistesPreferesPersistors{


    public static final String KEY_PREF = "ARTISTES_PREFERES";

    private SharedPreferences sharedPreferences;

    public AndroidArtistesPreferesPersistorImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void saveArtistesPreferes(Artist artist) {
        this.sharedPreferences.edit().putInt(artist.getNom(), artist.getNbClicks()).commit();
    }

    @Override
    public int readArtistesPreferes(String name) {
       return this.sharedPreferences.getInt(name, 0);
    }
}
