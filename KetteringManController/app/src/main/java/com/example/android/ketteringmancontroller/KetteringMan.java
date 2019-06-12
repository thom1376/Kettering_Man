package com.example.android.ketteringmancontroller;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;

public class KetteringMan extends Application {
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mBluetoothIsSupported = true;

    public BluetoothAdapter getmBluetoothAdapter() {
        if (mBluetoothAdapter == null && mBluetoothIsSupported) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothIsSupported = (mBluetoothAdapter != null);
        }
        return mBluetoothAdapter;
    }

    public boolean isBluetoothIsSupported() {
        return mBluetoothIsSupported;
    }
}
