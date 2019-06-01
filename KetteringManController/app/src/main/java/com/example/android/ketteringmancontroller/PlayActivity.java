package com.example.android.ketteringmancontroller;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.ketteringmancontroller.bluetooth.BluetoothFragment;
import com.example.android.ketteringmancontroller.bluetooth.BluetoothHelper;
import com.example.android.ketteringmancontroller.data.FirebaseDbHelper;

import java.util.Random;

public class PlayActivity extends AppCompatActivity
        implements LeaderboardDialogFragment.LeaderboardDialogListener,
        BluetoothFragment.BluetoothDialogListener {

    private final static String LOG_TAG = PlayActivity.class.getSimpleName();
    private TextView mScoreLabel;
    private FirebaseDbHelper mDbHelper;
    private BluetoothHelper mBluetoothHelper;

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
                Log.d(LOG_TAG, "Swipe right");
            }

            @Override
            public void onSwipeLeft() {
                Log.d(LOG_TAG, "Swipe left");
            }

            @Override
            public void onSwipeTop() {
                Log.d(LOG_TAG, "Swipe up");
            }

            @Override
            public void onSwipeBottom() {
                Log.d(LOG_TAG, "Swipe down");
            }
        });

        InitializeBluetooth();
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

    @Override
    public void onDialogDismiss(BluetoothFragment dialog) {
        if (mBluetoothHelper.isSupported()) {
            BluetoothSupported();
            if (mBluetoothHelper.isEnabled()) {
                BluetoothEnabled();
            } else {
                BluetoothNotEnabled();
            }
        } else {
            BluetoothNotSupported();
        }
    }

    private void InitializeBluetooth() {
        mBluetoothHelper = ((KetteringMan) this.getApplication()).getmBluetoothHelper();
        mBluetoothHelper.Initialize();
        if (mBluetoothHelper.isSupported()) {
            BluetoothSupported();
            if (!mBluetoothHelper.isEnabled()) {
                EnableBluetooth();
            } else {
                BluetoothEnabled();
            }
        } else {
            BluetoothNotSupported();
        }
    }

    private void EnableBluetooth() {
        DialogFragment bluetoothFragment = new BluetoothFragment();
        bluetoothFragment.show(getSupportFragmentManager(), "BluetoothFragment");
    }

    private void BluetoothSupported() {
        Log.d(LOG_TAG, "Bluetooth supported");
    }

    private void BluetoothEnabled() {
        Log.d(LOG_TAG, "Bluetooth enabled");
    }

    private void BluetoothNotSupported() {
        Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void BluetoothNotEnabled() {
        Log.d(LOG_TAG, "Bluetooth not enabled");
        finish();
    }

}
