package me.alfredobejarano.papiroplayer;

/**
 * Created by jacorona on 10/23/15.
 */
public class Song {
    private String title;
    private String artist;
    private String album;
    private String track;
    private String path;
    private byte[] cover;


    public Song(String title, String artist, String album, String track, String path, byte[] cover) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.track = track;
        this.path = path;
        this.cover = cover;
    }


    public String getTitle() {
        return title;
    }

    public String getNumber() {
        return track;
    }

    public String getPath() {
        return path;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }
}
