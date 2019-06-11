package com.example.android.ketteringmancontroller.bluetooth;

import android.bluetooth.BluetoothAdapter;

import com.google.firebase.database.annotations.NotNull;

public class BluetoothHelper {

    protected static final int REQUEST_ENABLE_BT = 1;
    private static BluetoothHelper mBluetoothHelper;
    private BluetoothAdapter mBluetoothAdapter = null;
    private boolean mIsInitialized;
    private boolean mIsSupported;
    private boolean mIsEnabled;

    private BluetoothHelper() {
        mIsInitialized = false;
        mIsSupported = true;
        mIsEnabled = false;
    }

    @NotNull
    public static BluetoothHelper getInstance() {
        if (mBluetoothHelper == null) {
            mBluetoothHelper = new BluetoothHelper();
        }
        return mBluetoothHelper;
    }

    public void Initialize() {
        if (mBluetoothAdapter == null)
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            mIsSupported = true;
            mIsEnabled = mBluetoothAdapter.isEnabled();
        } else {
            mIsSupported = false;
        }
        mIsInitialized = true;
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    public boolean isSupported() {
        return mIsSupported;
    }

    public boolean isEnabled() {
        return mIsEnabled;
    }

    protected void setIsEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
    }

    protected void setIsInitialized(boolean isInitialized) {
        this.mIsInitialized = isInitialized;
    }
}