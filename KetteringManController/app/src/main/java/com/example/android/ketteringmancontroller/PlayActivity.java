package com.example.android.ketteringmancontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.ketteringmancontroller.bluetooth.BluetoothFragment;
import com.example.android.ketteringmancontroller.data.FirebaseDbHelper;

import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class PlayActivity extends AppCompatActivity
        implements LeaderboardDialogFragment.LeaderboardDialogListener,
        BluetoothFragment.BluetoothDialogListener {

    private final static String LOG_TAG = PlayActivity.class.getSimpleName();
    private static final int REQUEST_PAIRED_DEVICE = 2;
    private TextView mScoreLabel;
    private FirebaseDbHelper mDbHelper;
    private BluetoothAdapter mBluetoothAdapter;

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
                Log.d(LOG_TAG, "Swipe right");
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(PlayActivity.this, "Swipe left", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Swipe left");
            }

            @Override
            public void onSwipeTop() {
                Toast.makeText(PlayActivity.this, "Swipe up", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Swipe up");
            }

            @Override
            public void onSwipeBottom() {
                Toast.makeText(PlayActivity.this, "Swipe down", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Swipe down");
            }
        });

        InitializeBluetooth();
    }

    //<editor-fold desc="Leaderboard methods">

    public static LeaderboardDialogFragment newLeaderboardFragment(int score) {
        LeaderboardDialogFragment leaderboardDialog = new LeaderboardDialogFragment();

        // Supply score as an argument.
        Bundle args = new Bundle();
        args.putInt("score", score);
        leaderboardDialog.setArguments(args);
        return leaderboardDialog;
    }

    @Override
    public void onLeaderboardDialogPositiveClick(LeaderboardDialogFragment dialog) {
        String initials = dialog.getInitials();
        int score = dialog.getScore();

        mDbHelper.insert(new LeaderboardItem(initials, score));
    }

    @Override
    public void onLeaderboardDialogNegativeClick(LeaderboardDialogFragment dialog) {

    }

    @Override
    public void onLeaderboardDialogDismiss(LeaderboardDialogFragment dialog) {
        finish();
    }

    //</editor-fold>

    //<editor-fold desc="Bluetooth methods">

    @Override
    public void onBluetoothDialogDismiss(BluetoothFragment dialog) {
        if (mBluetoothAdapter.isEnabled()) {
            BluetoothEnabled();
            //StartCommunication();
        } else {
            BluetoothNotEnabled();
        }
    }

    private void StartCommunication() {
        Set<BluetoothDevice> bluetoothDevices = mBluetoothAdapter.getBondedDevices();
        BluetoothDevice pacmanDevice = null;
        for (BluetoothDevice device : bluetoothDevices) {
            if (device.getName().equals("HC-05")) {
                pacmanDevice = device;
            }
        }
        if (pacmanDevice == null) {
            Toast.makeText(this, "No device named HC-05", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            BluetoothSocket socket = null;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                socket = pacmanDevice.createRfcommSocketToServiceRecord(UUID.randomUUID());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Socket's create() method failed", e);
            }

            if (socket != null) {
                // Cancel discovery because it otherwise slows down the connection.
                mBluetoothAdapter.cancelDiscovery();

                try {
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    socket.connect();
                    Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
                } catch (IOException connectException) {
                    // Unable to connect; close the socket and return.
                    try {
                        socket.close();
                    } catch (IOException closeException) {
                        Log.e(LOG_TAG, "Could not close the client socket", closeException);
                    }
                    Toast.makeText(this, "Could not connect", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void InitializeBluetooth() {
        mBluetoothAdapter = ((KetteringMan) this.getApplication()).getmBluetoothAdapter();
        if (((KetteringMan) this.getApplication()).isBluetoothIsSupported()) {
            BluetoothSupported();
            if (!mBluetoothAdapter.isEnabled()) {
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

    //</editor-fold>

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
