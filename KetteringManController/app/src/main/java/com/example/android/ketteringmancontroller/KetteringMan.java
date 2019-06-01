package com.example.android.ketteringmancontroller;

import android.app.Application;

import com.example.android.ketteringmancontroller.bluetooth.BluetoothHelper;

public class KetteringMan extends Application {
    private BluetoothHelper mBluetoothHelper;

    public BluetoothHelper getmBluetoothHelper() {
        if (mBluetoothHelper == null)
            mBluetoothHelper = BluetoothHelper.getInstance();
        return mBluetoothHelper;
    }
}
