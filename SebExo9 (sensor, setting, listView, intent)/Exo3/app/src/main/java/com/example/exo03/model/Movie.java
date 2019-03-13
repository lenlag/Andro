package com.example.exo03.model;

import java.io.Serializable;

public class Movie  implements Serializable {

    private String title;
    private String myImdb;
    private String year;
    private Real real;

    public Movie(String title, String myImdb, String year, Real real) {
        this.title = title;
        this.myImdb = myImdb;
        this.year = year;
        this.real = real;
    }
    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMyImdb() {
        return myImdb;
    }

    public void setMyImdb(String myImdb) {
        this.myImdb = myImdb;
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
        return "Movie{" +
                "title='" + title + '\'' +
                ", myImdb='" + myImdb + '\'' +
                ", year='" + year + '\'' +
                ", real=" + real +
                '}';
    }
}
