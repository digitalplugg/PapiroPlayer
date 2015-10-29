package me.alfredobejarano.papiroplayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private List<String> songs = new ArrayList<String>();
    private ArrayList<Song> foundedsongs = new ArrayList<Song>();
    private MediaPlayer mediaplayer = new MediaPlayer();
    private ArrayAdapter<String> songlist;
    private ListView songlistview;

    /* Constants */
    //private static final File SD_CARD_PATH = new File(new String(String.valueOf(Environment.getExternalStorageDirectory())+"/"));
    private static final File SD_CARD_PATH = new File("/storage/");
    private MP3Finder mp3finder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.getSupportActionBar().setTitle(getResources().getString(R.string.main_activity_title));

        /* Activity Code Starts Here */
        songlistview = (ListView) findViewById(R.id.songListView);


        try {
            foundedsongs = new ScanSongs(SD_CARD_PATH, MainActivity.this).execute().get();
            //songs = mp3finder.getSongsNames(foundedsongs);
            Log.d("###################",String.valueOf(foundedsongs.get(0).getTitle()));
        } catch (Exception e) {
            songs = new ArrayList<String>();
            songs.add(getResources().getString(R.string.empty_library));
            Log.d("###################", "Exception!" );
            e.printStackTrace();
        }

        songlist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songs);
        songlistview.setAdapter(songlist);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_equalizer) {
            Toast.makeText(this, getResources().getString(R.string.action_equalizer), Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_albums) {
            Toast.makeText(this, getResources().getString(R.string.album), Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_artist) {
            Toast.makeText(this, getResources().getString(R.string.artist), Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_genres) {
            Toast.makeText(this, getResources().getString(R.string.genres), Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_songs){
            Toast.makeText(this, getResources().getString(R.string.songs), Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

class ScanSongs extends AsyncTask< Void, Void, ArrayList<Song> > {
    private File file;
    private Context context;
    private ProgressDialog dialog;


    public ScanSongs(File file, Context context) {
        this.context = context;
        this.file = file;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(context.getString(R.string.loading_songs));
        dialog.show();
    }

    @Override
    protected ArrayList<Song> doInBackground(Void... params) {
        ArrayList<Song> songs = new ArrayList<>();
        MP3Finder finder = new MP3Finder();
        songs = finder.scanDirectory(file, context);
        return songs;
    }


    @Override
    protected void onPostExecute(ArrayList<Song> songs) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}



