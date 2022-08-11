package com.example.musiclibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import api.music.download.MusicApi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MusicApi.getInstance().init(this);
    }
}