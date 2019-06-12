package com.example.android.ketteringmancontroller.bluetooth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.android.ketteringmancontroller.KetteringMan;
import com.example.android.ketteringmancontroller.R;

public class BluetoothFragment extends DialogFragment {

    protected static final int REQUEST_ENABLE_BT = 1;
    // Use this instance of the interface to deliver action events
    private BluetoothDialogListener listener;
    private BluetoothAdapter mBluetoothAdapter;
    private AlertDialog mDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_bluetooth, null);

        builder.setView(v)
                .setPositiveButton("Ok", null);
        mDialog = builder.create();
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.leaderboard_dialog_bg);
        return mDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mBluetoothAdapter = ((KetteringMan) this.getActivity().getApplication()).getmBluetoothAdapter();
        if (((KetteringMan) this.getActivity().getApplication()).isBluetoothIsSupported()) {

        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    // Override the Fragment.onAttach() method to instantiate the BluetoothDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the LeaderboardDialogListener so we can send events to the host
            listener = (BluetoothDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().getClass().getName()
                    + " must implement " + BluetoothDialogListener.class.getSimpleName());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            listener.onBluetoothDialogDismiss(this);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get a reference to the dialog buttons
        Button positiveButton = mDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        // Set the text color of the dialog button text
        int textColor = ContextCompat.getColor(getContext(), android.R.color.white);
        positiveButton.setTextColor(textColor);

        // Get a reference to the parent view of the buttons and remove the spacing at the front
        LinearLayout parent = (LinearLayout) positiveButton.getParent();
        parent.setGravity(Gravity.CENTER_HORIZONTAL);

        View leftSpacer = parent.getChildAt(1);
        leftSpacer.setVisibility(View.GONE);

        View middleSpacer = parent.getChildAt(2);
        middleSpacer.setVisibility(View.GONE);

        // Center the buttons and give them a weight within the layout of 1
        LinearLayout.LayoutParams positiveButtonLayoutParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLayoutParams.weight = 1;
        positiveButtonLayoutParams.gravity = Gravity.CENTER;
        positiveButton.setLayoutParams(positiveButtonLayoutParams);

        // Set the background of the buttons as the button_animation drawable
        positiveButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_animation));
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface BluetoothDialogListener {
        void onBluetoothDialogDismiss(BluetoothFragment dialog);
    }
}
