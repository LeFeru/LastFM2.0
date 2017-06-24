package be.ipl.rasli.lastfm20.Models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import be.ipl.rasli.lastfm20.DTOs.Artist;

/**
 * Created by rachid on 15/06/17.
 */
public class ArtistsModel {
    private Set<Artist> artists;
    private List<OnArtistsModelChangeListener> listeners;

    public ArtistsModel() {
        this.artists = new TreeSet<Artist>(new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                return new Integer(o2.getNbEcoutes())-new Integer(o1.getNbEcoutes());
            }
        });

        this.listeners = new ArrayList<OnArtistsModelChangeListener>();
    }


    public ArtistsModel(Set<Artist> artists) {
        this.artists = artists;
        this.listeners = new ArrayList<OnArtistsModelChangeListener>();
    }

    public boolean ajouterArtist(Artist artist){
        if(this.artists.add(artist)){
            notify();
            return true;
        }
        return false;
    }

    public boolean ajouterArtists(Set<Artist> artists){
        if(this.artists.addAll(artists)){
            notify();
            return true;
        }
        return false;
    }

    public boolean ajouterArtists(List<Artist> artists){
        this.artists.removeAll(this.artists);
        if(this.artists.addAll(artists)){
            notifyOnArtistsModelChangeListeners();
            return true;
        }
        return false;
    }

    public List<Artist> getArtists(){

        Log.i("model", this.artists.toString());
        return new ArrayList<Artist>(this.artists);
    }

    private void notifyOnArtistsModelChangeListeners(){
        for(OnArtistsModelChangeListener listener : listeners){
            listener.onArtistsModelChange();
        }
    }

    public void ajouterListener(OnArtistsModelChangeListener listener) {
        this.listeners.add(listener);
    }
}

