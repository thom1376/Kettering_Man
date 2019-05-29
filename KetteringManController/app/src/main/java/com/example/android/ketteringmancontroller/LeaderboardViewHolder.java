package com.example.android.ketteringmancontroller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
    TextView mInitialsText, mScoreText;
    ImageView mImageView;

    public LeaderboardViewHolder(@NonNull View itemView) {
        super(itemView);

        mInitialsText = itemView.findViewById(R.id.initials_text);
        mScoreText = itemView.findViewById(R.id.score_text);
        mImageView = itemView.findViewById(R.id.item_image);
    }
}
