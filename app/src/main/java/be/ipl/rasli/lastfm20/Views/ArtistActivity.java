package be.ipl.rasli.lastfm20.Views;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import be.ipl.rasli.lastfm20.Builder.Builder;
import be.ipl.rasli.lastfm20.DTOs.Artist;
import be.ipl.rasli.lastfm20.Models.ArtistsModel;
import be.ipl.rasli.lastfm20.Persistors.AndroidArtistesPreferesPersistors;
import be.ipl.rasli.lastfm20.Persistors.AndroidColorsPersistors;
import be.ipl.rasli.lastfm20.R;

public class ArtistActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private ArtistsModel artistsModel;
    private AndroidArtistesPreferesPersistors androidArtistesPreferesPersistors;
    private AndroidColorsPersistors androidColorsPersistors;
    private Bundle savedInstanceState;
    private boolean tablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);
        this.tablet = false;
        this.savedInstanceState = savedInstanceState;
        this.artistsModel = ((Builder) this.getApplication()).getArtistsModel();
        this.androidColorsPersistors = ((Builder) this.getApplication()).getAndroidColorsPersistors();
        this.androidArtistesPreferesPersistors = ((Builder)this.getApplication()).getAndroidArtistesPreferesPersistors();
        addOrReplaceMasterFragment();
        if (this.findViewById(R.id.tablet600) != null) {
            this.tablet = true;
            addOrReplaceDetailsFragment();
        }
        Button btn = (Button) findViewById(R.id.btnArtistesPreferes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArtistActivity.this, ArtistesPreferesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextuel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Menu",item.getTitle().toString());
        if(item.getItemId() == R.id.noirEtBlanc){
            this.androidColorsPersistors.saveColor1(Color.BLACK);
            this.androidColorsPersistors.saveColor2(Color.WHITE);
        }
        else if(item.getItemId() == R.id.blancEtBleu){
            this.androidColorsPersistors.saveColor1(Color.WHITE);
            this.androidColorsPersistors.saveColor2(Color.BLUE);
        }
        else if(item.getItemId() == R.id.roseEtBrun){
            this.androidColorsPersistors.saveColor1(Color.parseColor("#ff69b4"));
            this.androidColorsPersistors.saveColor2(Color.parseColor("#8B4513"));
        }
        return super.onOptionsItemSelected(item);
    }

    private void addOrReplaceMasterFragment(){
        MasterFragment masterFragment = new MasterFragment();
        masterFragment.setArguments(getIntent().getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(this.savedInstanceState != null){
           masterFragment.setArguments(savedInstanceState);
        }
        if(getSupportFragmentManager().findFragmentById(R.id.fragment_master_artists) != null){
            if(!tablet){
                getSupportFragmentManager().popBackStack();
            }
            fragmentTransaction.replace(R.id.fragment_master_artists, masterFragment);
        }
        else{
            fragmentTransaction.add(R.id.fragment_master_artists, masterFragment);
        }
        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void addOrReplaceDetailsFragment(){
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(getIntent().getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(this.savedInstanceState != null){
            detailsFragment.setArguments(savedInstanceState);
        }
        if(getSupportFragmentManager().findFragmentById(R.id.fragment_details_artist) != null){
            fragmentTransaction.replace(R.id.fragment_details_artist, detailsFragment);
        }
        else{
            fragmentTransaction.add(R.id.fragment_details_artist, detailsFragment);
        }
        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Artist temp = this.artistsModel.getArtists().get(position);
        temp.setNbClicks(temp.getNbClicks()+1);
        this.androidArtistesPreferesPersistors.saveArtistesPreferes(temp);
        Log.i("temp",temp.toString());
        if(savedInstanceState == null){
            this.savedInstanceState = new Bundle();
        }
        savedInstanceState.putString(DetailsFragment.URL_KEY,temp.getUrl());
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_details_artist);
        if (detailsFragment != null && tablet) {
            Log.i("first if","ok");
            detailsFragment.loadNewWebPage(temp.getUrl());
        }
        else {
            Log.i("second if","ok");
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            DetailsFragment df1 = new DetailsFragment();
            df1.setArguments(getIntent().getExtras());
            ft1.addToBackStack(null);
            ft1.replace(R.id.fragment_master_artists, df1);
            ft1.commit();
            getSupportFragmentManager().executePendingTransactions();
            df1.loadNewWebPage(temp.getUrl());
        }
        return true;
    }

}
