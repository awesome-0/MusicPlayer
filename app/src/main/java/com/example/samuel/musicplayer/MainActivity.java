package com.example.samuel.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private SeekBar seekBar;
    private SongAdapter adapter;
    private ArrayList<Songs> songs = new ArrayList<>();
    MediaPlayer mediaPlayer;
    private static final int STORAGE_REQUEST = 100;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = (RecyclerView) findViewById(R.id.recyclerview);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        adapter = new SongAdapter(MainActivity.this, songs);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(recycler.getContext(), manager.getOrientation());
        recycler.addItemDecoration(decoration);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(manager);

        adapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(final Button b, View v, final Songs s, int position) {


                if (b.getText().toString().toLowerCase().equals("stop")) {
                    b.setText("Play");
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;

                } else {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {

                            try {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(s.getUrl());
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                        seekBar.setProgress(0);
                                        seekBar.setMax(mp.getDuration());
                                         }
                                });
                                b.setText("Stop");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    handler.postDelayed(r, 100);
                }
            }


        });


        checkForPermission();
        Thread t = new myThread();
        t.start();

    }

    public class myThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                seekBar.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    }
                });
            }
        }
    }

    private void checkForPermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST);

            }

        } else {
            loadSongs();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadSongs();

                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
                    checkForPermission();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    private void loadSongs() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    songs.add(new Songs(title, artist, data));
                }
                while (cursor.moveToNext());
            }
            adapter = new SongAdapter(MainActivity.this, songs);
        }

    }


}

