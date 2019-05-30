package com.example.android.ketteringmancontroller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class LeaderboardAdapter extends FirebaseRecyclerAdapter<LeaderboardItem, LeaderboardViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public LeaderboardAdapter(@NonNull FirebaseRecyclerOptions<LeaderboardItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position, @NonNull LeaderboardItem model) {
        holder.mInitialsText.setText(model.getInitials());
        holder.mScoreText.setText(String.format("%d", model.getScore()));

        switch (position) {
            case 0:
                holder.mImageView.setImageResource(R.drawable.ic_gold_medal);
                holder.mImageView.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.mImageView.setImageResource(R.drawable.ic_silver_medal);
                holder.mImageView.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.mImageView.setImageResource(R.drawable.ic_bronze_medal);
                holder.mImageView.setVisibility(View.VISIBLE);
                break;
            default:
                holder.mImageView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leaderboard_item, viewGroup, false);
        return new LeaderboardViewHolder(itemView);
    }
}
