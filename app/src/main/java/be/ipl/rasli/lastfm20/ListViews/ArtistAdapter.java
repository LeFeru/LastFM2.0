package be.ipl.rasli.lastfm20.ListViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import be.ipl.rasli.lastfm20.DTOs.Artist;
import be.ipl.rasli.lastfm20.R;

/**
 * Created by rachid on 15/06/17.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

        private int first = Color.GRAY, second = Color.WHITE;

        //artists est la liste des models à afficher
        public ArtistAdapter(Context context, List<Artist> artists, int first, int second){
            super(context, 0, artists);
            this.first = first;
            this.second = second;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_artist,parent, false);
            }

            ArtistViewHolder viewHolder = (ArtistViewHolder) convertView.getTag();
            if(viewHolder == null){
                viewHolder = new ArtistViewHolder();
                viewHolder.nomArtiste = (TextView) convertView.findViewById(R.id.nom_artiste);
                viewHolder.nbEcoutes = (TextView) convertView.findViewById(R.id.nb_ecoutes);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
                convertView.setTag(viewHolder);
            }

            //getItem(position) va récupérer l'item [position] de la List<Artist> artists
            Artist artist = getItem(position);

            //il ne reste plus qu'à remplir notre vue
            viewHolder.nomArtiste.setText(artist.getNom());
            viewHolder.nbEcoutes.setText(artist.getNbEcoutes());

            if (position % 2 == 1) {
                convertView.setBackgroundColor(first);
            } else {
                convertView.setBackgroundColor(second);
            }
            if (viewHolder.imageView != null) {
                new ImageDownloaderTask(viewHolder.imageView).execute(artist.getUrlImage());
            }
            return convertView;
        }

        private class ArtistViewHolder{
            public TextView nomArtiste;
            public TextView nbEcoutes;
            public ImageView imageView;
        }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

}
