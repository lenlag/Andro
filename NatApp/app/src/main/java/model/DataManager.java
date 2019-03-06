package model;

import java.io.Serializable;

public class DataManager implements Serializable {


    public Movie[] getMovie() {
        Real[] reals = new Real[3]; //taille effective - vrai taille entre []
        reals[0] = new Real();
        reals[0].setName("Fritz Lang");
        reals[0].setImdb("nm0000485");
        reals[1] = new Real();
        reals[1].setName("Ethan Cohen");
        reals[1].setImdb("nm0001053");
        reals[2] = new Real();
        reals[2].setName("David Fincher");
        reals[2].setImdb("nm0000399");

        Movie[] movies = new Movie[3];
        movies[0] = new Movie();
        movies[0].setTitle("M Le Maudit");
        movies[0].setImdb("tt0022100");
        movies[0].setReal(reals[0]);
        movies[0].setYear("1931");
        movies[1] = new Movie();
        movies[1].setTitle("Fargo");
        movies[1].setImdb("tt0116282");
        movies[1].setReal(reals[1]);
        movies[1].setYear("1996");
        movies[2] = new Movie();
        movies[2].setTitle("Seven");
        movies[2].setImdb("tt0114369");
        movies[2].setReal(reals[2]);
        movies[2].setYear("1995");

        return movies;
    }
}
