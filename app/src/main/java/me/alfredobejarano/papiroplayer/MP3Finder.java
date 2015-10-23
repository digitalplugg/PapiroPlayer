package me.alfredobejarano.papiroplayer;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jacorona on 10/23/15.
 */
public class MP3Finder {

    private File SD_PATH;
    private String name;
    private String artist;
    private String album;
    private String track;
    private String songpath;
    private byte[] cover;
    private Song song;

    private ArrayList<Song> songs = new ArrayList<Song>();
    private final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

    public MP3Finder(){}

    private void findMySongs(File path) {
        File[] files = path.listFiles(new MP3Filter());
        if(files != null) {
            for(File file : files) {
                /*Obtaining the MP3*/
                mediaMetadataRetriever.setDataSource(file.getAbsolutePath());

                /*Obtaining Metadata*/
                name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                songpath = file.getAbsolutePath();
                track = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
                cover = mediaMetadataRetriever.getEmbeddedPicture();

                /*Creating a new song*/
                song = new Song(name, artist, album, track, songpath, cover);

                /*Adding the song*/
                songs.add(song);

                /*Resetting variables*/
                name = null;
                artist = null;
                album = null;
                songpath = null;
                track = null;
                cover = null;
                song = null;
            }
        }
    }

    public ArrayList<Song> scanDirectory(File path, Context context) {
        Song error = new Song(context.getResources().getString(R.string.empty_library), null, null, null, null, null);

        if(path != null) {
            File[] files = path.listFiles();
            if(files != null && files.length > 0) {
                for(File file : files) {
                    Log.d("",file.getName());
                    if(file.isDirectory()) {
                        scanDirectory(path, context);
                    } else {
                        findMySongs(file);
                    }
                }
            }
        } else {
            songs.add(error);
        }

        return getFoundedSongs();
    }


    private ArrayList<Song> getFoundedSongs() {
        return songs;
    }

    /* Retrieve metadata from all songs easily */

    public ArrayList<String> getSongsNames(ArrayList<Song> songs){
        /*
            ########################/\
            #######################/  \
            ######################/    \
            ##################### ------
            Just for here, shhhhh ( (º) )
        */
        ArrayList<String> illuminattisongnames = new ArrayList<String>();

        for(Song song : songs) {
            String title;

            /* I don't always listen to music in my Android phone, but when I do, I leave all the Metadata blank */

            title = song.getTitle();

            if(title == null) {
                title = song.getPath()+" (untitled)";
            }

            illuminattisongnames.add(title);
        }

        return illuminattisongnames;
    }

    /* Get only album names */
    public ArrayList<String> getAlbumNames(ArrayList<Song> songs){
        /*
            ########################/\
            #######################/  \
            ######################/    \
            ##################### ------
            Just for here, shhhhh ( (º) )
        */
        ArrayList<String> illuminattialbumnames = new ArrayList<String>();

        /* We need to get all the albums independently */
        ArrayList<String> unsortedalbumnames = new ArrayList<String>();

        for(int i = 0; i <= songs.size()-1; i++) {
            String album = songs.get(i).getAlbum();

            if(album == null) {
                album = " ";
            }

            unsortedalbumnames.add(album);

            album = null;
        }

        Set<String> sortedalbumnames = new HashSet<>();

        sortedalbumnames.addAll(unsortedalbumnames);

        illuminattialbumnames.addAll(sortedalbumnames);

        return illuminattialbumnames;
    }


    /* ################################ */

    /*Find songs by album */
    public ArrayList<ArrayList<Song>> findAlbums(ArrayList<Song> songs) {

        ArrayList<ArrayList<Song>> albums = new ArrayList<ArrayList<Song>>();
        ArrayList<Song> currentalbum = new ArrayList<Song>();
        String currentsearchedalbum;
        String album;

        /* Get all album names in phone */
        ArrayList<String> availablealbumnames = getAlbumNames(songs);

        for (int i = 0; i <= availablealbumnames.size() -1; i++) {
            currentsearchedalbum = availablealbumnames.get(i);

            for(Song song : songs) {
                album = song.getAlbum();

                if(album == null) {
                    album = " ";
                }

                if(album.equals(currentsearchedalbum)) {
                    currentalbum.add(song);
                }
            }

            albums.add(currentalbum);

            currentalbum.clear();
        }

        return albums;
    }

    /* Get only artist names */
    public ArrayList<String> getArtistNames(ArrayList<Song> songs){

        ArrayList<String> artistnames = new ArrayList<String>();

        /* We need to get all the artist independently */
        ArrayList<String> uartistnames = new ArrayList<String>();

        for(int i = 0; i <= songs.size()-1; i++) {
            String artist = songs.get(i).getArtist();

            if(artist == null) {
                artist = " ";
            }

            uartistnames.add(artist);

            artist = null;
        }

        Set<String> sartistnames = new HashSet<>();

        sartistnames.addAll(uartistnames);

        artistnames.addAll(sartistnames);

        return artistnames;
    }


    /* ################################ */

    /*Find songs by artist */
    public ArrayList<ArrayList<Song>> findArtist(ArrayList<Song> songs) {

        ArrayList<ArrayList<Song>> artists = new ArrayList<ArrayList<Song>>();
        ArrayList<Song> cartist = new ArrayList<Song>();
        String csartist;
        String artist;

        /* Get all artist names in phone */
        ArrayList<String> aartist = getArtistNames(songs);

        for (int i = 0; i <= aartist.size() -1; i++) {
            csartist = aartist.get(i);

            for(Song song : songs) {
                artist = song.getArtist();

                if(artist == null) {
                    artist = " ";
                }

                if(artist.equals(csartist)) {
                    cartist.add(song);
                }
            }

            artists.add(cartist);

            cartist.clear();
        }

        return artists;
    }
}
