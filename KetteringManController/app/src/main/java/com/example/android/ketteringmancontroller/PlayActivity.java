package com.example.android.ketteringmancontroller;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.ketteringmancontroller.data.FirebaseDbHelper;

import java.util.Random;

public class PlayActivity extends AppCompatActivity
        implements LeaderboardDialogFragment.LeaderboardDialogListener {

    TextView mScoreLabel;
    FirebaseDbHelper mDbHelper;
    OnSwipeGestureListener swipeGestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mScoreLabel = findViewById(R.id.score_label);
        mScoreLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                DialogFragment leaderboardDialog = newLeaderboardFragment(r.nextInt(1000));
                leaderboardDialog.show(getSupportFragmentManager(), "LeaderboardDialogFragment");
            }
        });

        mDbHelper = new FirebaseDbHelper(null);
        findViewById(R.id.swipe_area).setOnTouchListener(new OnSwipeGestureListener(this) {
            @Override
            public void onSwipeRight() {
                Toast.makeText(PlayActivity.this, "Swipe right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(PlayActivity.this, "Swipe left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeTop() {
                Toast.makeText(PlayActivity.this, "Swipe up", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeBottom() {
                Toast.makeText(PlayActivity.this, "Swipe down", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDialogPositiveClick(LeaderboardDialogFragment dialog) {
        String initials = dialog.getInitials();
        int score = dialog.getScore();

        mDbHelper.insert(new LeaderboardItem(initials, score));
    }

    public static LeaderboardDialogFragment newLeaderboardFragment(int score) {
        LeaderboardDialogFragment leaderboardDialog = new LeaderboardDialogFragment();

        // Supply score as an argument.
        Bundle args = new Bundle();
        args.putInt("score", score);
        leaderboardDialog.setArguments(args);
        return leaderboardDialog;
    }

    @Override
    public void onDialogNegativeClick(LeaderboardDialogFragment dialog) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onDialogDismiss(LeaderboardDialogFragment dialog) {
        finish();
    }
}
