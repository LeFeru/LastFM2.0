package be.ipl.rasli.lastfm20.Builder;

import android.app.Application;

import be.ipl.rasli.lastfm20.Models.ArtistsModel;
import be.ipl.rasli.lastfm20.Persistors.AndroidArtistesPreferesPersistorImpl;
import be.ipl.rasli.lastfm20.Persistors.AndroidArtistesPreferesPersistors;
import be.ipl.rasli.lastfm20.Persistors.AndroidColorsPersistors;
import be.ipl.rasli.lastfm20.Persistors.AndroidColorsPersistorsImpl;

/**
 * Created by rachid on 15/06/17.
 */
public class Builder extends Application {

    private ArtistsModel artistsModel;
    private AndroidColorsPersistors androidColorsPersistors;
    private AndroidArtistesPreferesPersistors androidArtistesPreferesPersistors;

    @Override
    public void onCreate() {
        super.onCreate();
        this.artistsModel = new ArtistsModel();
        this.androidColorsPersistors = new AndroidColorsPersistorsImpl(getApplicationContext().getSharedPreferences(AndroidColorsPersistorsImpl.KEY_PREF, MODE_PRIVATE));
        this.androidArtistesPreferesPersistors = new AndroidArtistesPreferesPersistorImpl(getApplicationContext().getSharedPreferences(AndroidArtistesPreferesPersistorImpl.KEY_PREF, MODE_PRIVATE));
    }

    public ArtistsModel getArtistsModel() {
        return artistsModel;
    }

    public AndroidColorsPersistors getAndroidColorsPersistors() {
        return androidColorsPersistors;
    }

    public AndroidArtistesPreferesPersistors getAndroidArtistesPreferesPersistors() {
        return androidArtistesPreferesPersistors;
    }
}
