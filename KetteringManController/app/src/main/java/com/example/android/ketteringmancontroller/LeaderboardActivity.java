package com.example.android.ketteringmancontroller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Random;

public class LeaderboardActivity extends AppCompatActivity {
    private static final int MAX_NUM_ITEMS = 10;

    private EmptyRecyclerView mRecyclerView;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseRecyclerOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));
        mRecyclerView.setHasFixedSize(true);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("LEADERBOARD_FIREBASE");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void add(View view) {
        Random random = new Random();
        int score = random.nextInt(1000);
        Toast.makeText(this, "Score: " + score, Toast.LENGTH_SHORT).show();
        if (adapter != null) {
            LeaderboardItem entry = new LeaderboardItem("AAA", score);
            mDatabaseReference.push().setValue(entry, 0 - entry.getScore());
            adapter.notifyDataSetChanged();
        }
    }

    public void remove(View view) {
        Query query = mDatabaseReference.limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterator<DataSnapshot> children = dataSnapshot.getChildren().iterator();
                    if (children.hasNext()) {
                        DataSnapshot snapshot = children.next();
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void createAdapter() {
        options =
                new FirebaseRecyclerOptions.Builder<LeaderboardItem>()
                        .setQuery(mDatabaseReference.limitToFirst(MAX_NUM_ITEMS), LeaderboardItem.class)
                        .build();
        adapter =
                new FirebaseRecyclerAdapter<LeaderboardItem, LeaderboardViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position, @NonNull final LeaderboardItem model) {
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
                        View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.leaderboard_item, viewGroup, false);
                        return new LeaderboardViewHolder(itemView);
                    }
                };
        mRecyclerView.setAdapter(adapter);
    }
}
