package com.example.android.ketteringmancontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.android.ketteringmancontroller.data.FirebaseDbHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Random;

public class LeaderboardActivity extends AppCompatActivity implements FirebaseDbHelper.DatabaseChangeListener {

    private EmptyRecyclerView mRecyclerView;
    private FirebaseDbHelper mDbHelper;
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

        mDbHelper = new FirebaseDbHelper(this);

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
        Random r = new Random();
        LeaderboardItem item = new LeaderboardItem("AAA", r.nextInt(1000));
        mDbHelper.insert(item);
    }

    public void remove(View view) {
        if (adapter == null) {
            return;
        }
        if (adapter.getItemCount() > 0) {
            String key = adapter.getRef(0).getKey();
            mDbHelper.delete(key);
        }
    }

    private void createAdapter() {
        options = new FirebaseRecyclerOptions.Builder<LeaderboardItem>()
                .setQuery(mDbHelper.getQuery(), LeaderboardItem.class)
                        .build();
        adapter = new LeaderboardAdapter(options);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDataChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}
