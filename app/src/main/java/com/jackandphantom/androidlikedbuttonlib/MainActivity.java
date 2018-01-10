package com.jackandphantom.androidlikedbuttonlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidLikeButton androidLikeButton = findViewById(R.id.myView);
        androidLikeButton.setCurrentlyLiked(true);

        androidLikeButton.setOnLikeEventListener(new AndroidLikeButton.OnLikeEventListener() {
            @Override
            public void onLikeClicked(AndroidLikeButton androidLikeButton) {

            }

            @Override
            public void onUnlikeClicked(AndroidLikeButton androidLikeButton) {

            }
        });

    }
}
