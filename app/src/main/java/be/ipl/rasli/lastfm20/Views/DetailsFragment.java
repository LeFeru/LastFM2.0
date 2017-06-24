package be.ipl.rasli.lastfm20.Views;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

import be.ipl.rasli.lastfm20.R;

/**
 * Created by rachid on 15/06/17.
 */
public class DetailsFragment extends Fragment{


    private WebView mWebView;
    private String url;
    public static final String URL_KEY = "URL_KEY";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("onCV","ok");
        View v = inflater.inflate(R.layout.fragment_details_artist, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("onVC","ok");
        savedInstanceState = getArguments();
        if (savedInstanceState == null){
            return;
        }
        if(savedInstanceState.getString(URL_KEY) != null) {
            this.url = savedInstanceState.getString(URL_KEY);
            Log.i("onViewCreated",savedInstanceState.getString(URL_KEY));
        } else {
            this.url = "https://www.last.fm/music/";
        }

        mWebView.loadUrl(url);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            Log.i("onActivityCreated",savedInstanceState.getString(URL_KEY));
            if(savedInstanceState.getString(URL_KEY) != null) {
                this.url = savedInstanceState.getString(URL_KEY);
            }
        }
        if (this.url == null){
            this.url = "https://www.last.fm/music/";
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(URL_KEY, this.url);
    }
    public void loadNewWebPage(String url) {
        Log.i("load",url);
        this.url = url;
        mWebView.loadUrl(url);
    }
}
