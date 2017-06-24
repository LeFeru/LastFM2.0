package be.ipl.rasli.lastfm20.Views;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import be.ipl.rasli.lastfm20.Builder.Builder;
import be.ipl.rasli.lastfm20.DTOs.Artist;
import be.ipl.rasli.lastfm20.ListViews.ArtistAdapter;
import be.ipl.rasli.lastfm20.Models.ArtistsModel;
import be.ipl.rasli.lastfm20.Models.OnArtistsModelChangeListener;
import be.ipl.rasli.lastfm20.Persistors.AndroidColorsPersistors;
import be.ipl.rasli.lastfm20.Persistors.OnColorsDataChangeListener;
import be.ipl.rasli.lastfm20.R;

public class ArtistesPreferesActivity extends AppCompatActivity implements OnArtistsModelChangeListener, OnColorsDataChangeListener {

    private ArtistsModel artistsModel;
    private ListView artistsListView;
    private ArtistAdapter adapter;
    private List<Artist> artists;
    private AndroidColorsPersistors androidColorsPersistors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistes_preferes);
        this.androidColorsPersistors = ((Builder) this.getApplication()).getAndroidColorsPersistors();
        this.androidColorsPersistors.addObserver(this);
        this.artistsModel = ((Builder)getApplication()).getArtistsModel();
        this.artists = new ArrayList<Artist>();
        this.artistsListView = (ListView) findViewById(R.id.listViewPrefs);
        this.adapter = new ArtistAdapter(getApplicationContext(), this.artists, this.androidColorsPersistors.readColor1(), this.androidColorsPersistors.readColor2());
        this.artistsListView.setAdapter(this.adapter);
        onArtistsModelChange();
    }

    @Override
    public void onArtistsModelChange() {
        this.artists.removeAll(this.artists);
        this.artists.addAll(this.artistsModel.getArtists());
        Collections.sort(this.artists,new Comparator<Artist>(){
            @Override
            public int compare(Artist o1, Artist o2) {
                return o2.getNbClicks()-o1.getNbClicks();
            }
        });
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onColorsDataChange() {
        this.adapter = new ArtistAdapter(getApplicationContext(), this.artists, this.androidColorsPersistors.readColor1(), this.androidColorsPersistors.readColor2());
        this.artistsListView.setAdapter(this.adapter);
    }
}
