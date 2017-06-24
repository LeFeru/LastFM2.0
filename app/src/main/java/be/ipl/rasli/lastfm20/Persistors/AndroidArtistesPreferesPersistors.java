package be.ipl.rasli.lastfm20.Persistors;

import be.ipl.rasli.lastfm20.DTOs.Artist;

/**
 * Created by rachid on 19/06/17.
 */
public interface AndroidArtistesPreferesPersistors {

    public void saveArtistesPreferes(Artist artist);

    public int readArtistesPreferes(String name);

}
