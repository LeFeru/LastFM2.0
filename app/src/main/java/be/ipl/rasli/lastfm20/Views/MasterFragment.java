package be.ipl.rasli.lastfm20.Views;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.net.ssl.HttpsURLConnection;

import be.ipl.rasli.lastfm20.Builder.Builder;
import be.ipl.rasli.lastfm20.DTOs.Artist;
import be.ipl.rasli.lastfm20.ListViews.ArtistAdapter;
import be.ipl.rasli.lastfm20.Models.ArtistsModel;
import be.ipl.rasli.lastfm20.Models.OnArtistsModelChangeListener;
import be.ipl.rasli.lastfm20.Persistors.AndroidArtistesPreferesPersistors;
import be.ipl.rasli.lastfm20.Persistors.AndroidColorsPersistors;
import be.ipl.rasli.lastfm20.Persistors.OnColorsDataChangeListener;
import be.ipl.rasli.lastfm20.R;

/**
 * Created by rachid on 15/06/17.
 */
public class MasterFragment extends Fragment implements OnArtistsModelChangeListener, OnColorsDataChangeListener {

    public static final String LIST_ARTISTS_KEY = "LIST_ARTISTS";
    public static final String URL = "http://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&country=belgium&api_key=32ef5df0e36797b605e205529058f3b8&format=json&limit=";

    private View view;
    private AdapterView.OnItemLongClickListener toCallback;
    private ArtistsModel artistsModel;
    private ListView artistsListView;
    private ArtistAdapter adapter;
    private List<Artist> artists;
    private ConnectivityManager connectivityManager;
    private AndroidColorsPersistors androidColorsPersistors;
    private AndroidArtistesPreferesPersistors androidArtistesPreferesPersistors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_master_artists, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        this.androidColorsPersistors = ((Builder) getActivity().getApplication()).getAndroidColorsPersistors();
        this.androidColorsPersistors.addObserver(this);
        this.androidArtistesPreferesPersistors = ((Builder)getActivity().getApplication()).getAndroidArtistesPreferesPersistors();
        this.artistsModel = ((Builder) view.getContext().getApplicationContext()).getArtistsModel();
        this.artistsModel.ajouterListener(this);
        this.artists = new ArrayList<Artist>();
        this.artistsListView = (ListView) view.findViewById(R.id.listView);
        this.adapter = new ArtistAdapter(view.getContext(), this.artists,this.androidColorsPersistors.readColor1(),this.androidColorsPersistors.readColor2());
        this.artistsListView.setAdapter(this.adapter);
        this.artistsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return toCallback.onItemLongClick(parent, view, position, id);
            }
        });
        Log.i("size",this.artistsModel.getArtists().size()+"");
        if(this.artistsModel.getArtists().size() == 0){
            Log.i("needDownload","true");
            this.connectivityManager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                //boutonQuery.setEnabled(false);
                new MyTask().execute(URL + 50);
                //boutonQuery.setEnabled(true);
            } else {
                Toast.makeText(getContext(), "Pas de connexion Internet !",
                        Toast.LENGTH_LONG).show();
            }
        }
        else{
            Log.i("needDownload","false");
            onArtistsModelChange();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            toCallback = (AdapterView.OnItemLongClickListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toCallback = null;
    }

    @Override
    public void onArtistsModelChange() {
        this.artists.removeAll(this.artists);
        this.artists.addAll(this.artistsModel.getArtists());
        this.adapter.notifyDataSetChanged();
    }
    //City Girl Power
    //Soutien Région, Ville, Fédération Wallonie-Bruxelles, l'échevin Alain Courtois, ministre des sports Rachid Madrane
    //mail
    //fb
    //200€
    //à 12 ans
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        this.artists = savedInstanceState.getParcelableArrayList(LIST_ARTISTS_KEY);
        this.artistsModel = ((Builder) this.view.getContext().getApplicationContext()).getArtistsModel();
        this.artistsModel.ajouterListener(this);
        this.connectivityManager = (ConnectivityManager) this.view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        this.artistsListView = (ListView) this.view.findViewById(R.id.listView);
        this.adapter = new ArtistAdapter(view.getContext(), this.artists,this.androidColorsPersistors.readColor1(),this.androidColorsPersistors.readColor2());
        this.artistsListView.setAdapter(this.adapter);
    }


    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_ARTISTS_KEY, (ArrayList<? extends Parcelable>) this.artists);
    }

    @Override
    public void onColorsDataChange() {
        this.adapter = new ArtistAdapter(view.getContext(), this.artists,this.androidColorsPersistors.readColor1(),this.androidColorsPersistors.readColor2());
        this.artistsListView.setAdapter(this.adapter);
    }
    // Here is the AsyncTask class:
    //
    // AsyncTask<Params, Progress, Result>.
    //    Params – the type (Object/primitive) you pass to the AsyncTask from .execute()
    //    Progress – the type that gets passed to onProgressUpdate()
    //    Result – the type returns from doInBackground()
    // Any of them can be String, Integer, Void, etc.

    private class MyTask extends AsyncTask<String, Integer, List<Artist>> {


        // This is run in a background thread
        @Override
        protected List<Artist> doInBackground(String... params) {
            String url = params[0];
            try {
                String result = downloadUrl(new URL(url));
                return traiterJSON(result);
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<Artist>();
            }
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(List<Artist> result) {
            super.onPostExecute(result);
            artistsModel.ajouterArtists(result);
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */
        private String downloadUrl(URL url) throws IOException {
            InputStream stream = null;
            HttpURLConnection connection = null;
            String result = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                if (stream != null) {
                    result = readStream(stream);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        private String readStream(InputStream is) throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            is.close();
            return sb.toString();
        }

        private List<Artist> traiterJSON(String json) {

            Log.d("res", json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject topArtists = (JSONObject) jsonObject.get("topartists");
                JSONArray artists = (JSONArray) topArtists.get("artist");
                List<Artist> liste = new ArrayList<Artist>();
                for (int i = 0; i < artists.length(); i++) {
                    JSONObject artist = (JSONObject) artists.get(i);
                    JSONArray image = (JSONArray) artist.get("image");
                    JSONObject urlImage = (JSONObject) image.get(1);
                    liste.add(new Artist(artist.getString("name"), artist.getString("listeners"), artist.getString("url"), androidArtistesPreferesPersistors.readArtistesPreferes(artist.getString("name")),urlImage.getString("#text")));

                }
                Log.d("liste", liste.toString());
                return liste;
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<Artist>();
            }
        }
    }
}
