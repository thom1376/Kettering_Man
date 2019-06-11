package com.example.android.ketteringmancontroller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LeaderboardDialogFragment extends DialogFragment {

    // Use this instance of the interface to deliver action events
    LeaderboardDialogListener listener;
    private String mInitials;
    private int mScore;
    private AlertDialog mDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        mScore = args.getInt("score", 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_leaderboard, null);

        TextView scoreText = v.findViewById(R.id.score_text);
        scoreText.setText(String.valueOf(mScore));

        builder.setView(v)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText initialsText = v.findViewById(R.id.initials_text);
                        mInitials = initialsText.getText().toString();

                        if (!mInitials.isEmpty())
                            listener.onLeaderboardDialogPositiveClick(LeaderboardDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onLeaderboardDialogNegativeClick(LeaderboardDialogFragment.this);
                    }
                });
        mDialog = builder.create();
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.leaderboard_dialog_bg);
        return mDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onLeaderboardDialogDismiss(this);
    }

    // Override the Fragment.onAttach() method to instantiate the LeaderboardDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the LeaderboardDialogListener so we can send events to the host
            listener = (LeaderboardDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().getClass().getName()
                    + " must implement LeaderboardDialogListener");
        }
    }

    public String getInitials() {
        return mInitials;
    }

    public int getScore() {
        return mScore;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get a reference to the dialog buttons
        Button positiveButton = mDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = mDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        // Set the text color of the dialog button text
        int textColor = ContextCompat.getColor(getContext(), android.R.color.white);
        positiveButton.setTextColor(textColor);
        negativeButton.setTextColor(textColor);

        // Get a reference to the parent view of the buttons and remove the spacing at the front
        LinearLayout parent = (LinearLayout) positiveButton.getParent();
        parent.setGravity(Gravity.CENTER_HORIZONTAL);
        View leftSpacer = parent.getChildAt(1);
        leftSpacer.setVisibility(View.GONE);

        // Center the buttons and give them a weight within the layout of 1
        LinearLayout.LayoutParams positiveButtonLayoutParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLayoutParams.weight = 1;
        positiveButtonLayoutParams.gravity = Gravity.CENTER;
        positiveButton.setLayoutParams(positiveButtonLayoutParams);

        LinearLayout.LayoutParams negativeButtonLayoutParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeButtonLayoutParams.weight = 1;
        negativeButtonLayoutParams.gravity = Gravity.CENTER;
        negativeButton.setLayoutParams(negativeButtonLayoutParams);

        // Set the background of the buttons as the button_animation drawable
        positiveButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_animation));
        negativeButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_animation));
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface LeaderboardDialogListener {
        void onLeaderboardDialogPositiveClick(LeaderboardDialogFragment dialog);

        void onLeaderboardDialogNegativeClick(LeaderboardDialogFragment dialog);

        void onLeaderboardDialogDismiss(LeaderboardDialogFragment dialog);
    }
}
