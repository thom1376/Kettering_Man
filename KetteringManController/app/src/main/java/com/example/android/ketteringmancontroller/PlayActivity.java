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

    public static LeaderboardDialogFragment newLeaderboardFragment(int score) {
        LeaderboardDialogFragment leaderboardDialog = new LeaderboardDialogFragment();

        // Supply score as an argument.
        Bundle args = new Bundle();
        args.putInt("score", score);
        leaderboardDialog.setArguments(args);
        return leaderboardDialog;
    }

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
    }

    @Override
    public void onDialogPositiveClick(LeaderboardDialogFragment dialog) {
        String initials = dialog.getInitials();
        int score = dialog.getScore();

        mDbHelper.insert(new LeaderboardItem(initials, score));
        Toast.makeText(this, "Initials: " + initials + " Score: " + score, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(LeaderboardDialogFragment dialog) {
        Toast.makeText(this, "onDialogNegativeClick", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
