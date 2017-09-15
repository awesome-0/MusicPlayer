package com.example.samuel.musicplayer;

/**
 * Created by Samuel on 15/09/2017.
 */

public class Songs {

    private String name, artist,url;

    public Songs() {
    }

    public Songs(String name, String artist, String url) {
        this.name = name;
        this.artist = artist;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
