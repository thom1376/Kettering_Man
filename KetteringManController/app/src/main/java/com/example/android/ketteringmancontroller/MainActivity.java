package com.example.android.ketteringmancontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mPlayButton;
    Button mLeaderboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayButton = findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the play activity
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivityWithAnimation(intent);
            }
        });

        mLeaderboardButton = findViewById(R.id.leaderboard_button);
        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the leaderboard activity
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivityWithAnimation(intent);
            }
        });
    }

    public void startActivityWithAnimation(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
