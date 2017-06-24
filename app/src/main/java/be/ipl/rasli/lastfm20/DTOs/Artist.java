package be.ipl.rasli.lastfm20.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by rachid on 15/06/17.
 */
public class Artist implements Parcelable {
    private String nom;
    private String nbEcoutes;
    private String url;
    private int nbClicks;
    private String urlImage;

    public Artist(Parcel in) {
        this.nom = in.readString();
        this.nbEcoutes = in.readString();
        this.url = in.readString();
        this.nbClicks = in.readInt();
        this.urlImage = in.readString();
    }
    public Artist(String nom, String nbEcoutes, String url, int nbClicks, String urlImage) {
        this.nom = nom;
        this.nbEcoutes = nbEcoutes;
        this.url = url;
        this.nbClicks = nbClicks;
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public static final Parcelable.Creator<Artist> CREATOR
            = new Parcelable.Creator<Artist>() {

        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    @Override
    public String toString() {
        return "Artist{" +
                "nom='" + nom + '\'' +
                ", nbEcoutes='" + nbEcoutes + '\'' +
                ", url='" + url + '\'' +
                ", nbClicks=" + nbClicks +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;

        Artist artist = (Artist) o;

        return nom.equals(artist.nom);

    }


    public int getNbClicks() {
        return nbClicks;
    }

    public void setNbClicks(int nbClicks) {
        this.nbClicks = nbClicks;
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNbEcoutes(String nbEcoutes) {
        this.nbEcoutes = nbEcoutes;
    }

    public String getNom() {
        return nom;
    }

    public String getNbEcoutes() {
        return nbEcoutes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(nbEcoutes);
        dest.writeString(url);
        dest.writeInt(nbClicks);
        dest.writeString(urlImage);
    }
}
