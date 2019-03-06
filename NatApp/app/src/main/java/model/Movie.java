package model;

import java.io.Serializable;

//classe doit implementer Serializable, sinon impossible de passer l'objet dans une nouvelle activité
public class Movie implements Serializable {

    private String title;
    private String imdb;
    private String year;
    private Real real;

    public Movie() {

    }

    public Movie(String title, String imdb, String year, Real real) {
        this.title = title;
        this.imdb = imdb;
        this.year = year;
        this.real = real;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Real getReal() {
        return real;
    }

    public void setReal(Real real) {
        this.real = real;
    }

    @Override
    public String toString() {
        return title;
    }
}
