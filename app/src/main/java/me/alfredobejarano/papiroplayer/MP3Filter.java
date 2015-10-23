package me.alfredobejarano.papiroplayer;

import java.io.File;
import java.io.FilenameFilter;

public class MP3Filter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String filename) {
        return (filename.endsWith(".mp3"));
    }
}