package be.ipl.rasli.lastfm20.Persistors;

/**
 * Created by rachid on 19/06/17.
 */
public interface AndroidColorsPersistors {

    public void saveColor1(int color);

    public void saveColor2(int color);

    public int readColor1();

    public int readColor2();

    public void notifyObservers();

    public void addObserver(OnColorsDataChangeListener observer);
}
