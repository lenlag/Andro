package com.example.exo03.model;

import java.io.Serializable;

public class Real implements Serializable {

    private String name;
    private String imdb;

    public Real(String name, String imdb) {
        this.name = name;
        this.imdb = imdb;
    }

    public Real() {
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
}
