package model;

import java.io.Serializable;

public class Real implements Serializable {

    private String name;

    private String imdb;

    public Real() {

    }

    public Real(String name, String imdb) {
        this.name = name;
        this.imdb = imdb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    @Override
    public String toString() {
        return "Real{" +
                "name='" + name + '\'' +
                ", imdb='" + imdb + '\'' +
                '}';
    }
}
