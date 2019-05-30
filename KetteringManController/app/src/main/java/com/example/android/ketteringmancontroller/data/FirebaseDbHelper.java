package com.example.android.ketteringmancontroller.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.ketteringmancontroller.LeaderboardItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class FirebaseDbHelper {

    private final static String LOG_TAG = FirebaseDbHelper.class.getSimpleName();
    private final static String TABLE_NAME = "LEADERBOARD_FIREBASE";
    private final static int MAX_NUM_ITEMS = 10;

    private DatabaseChangeListener listener;
    private DatabaseReference mDatabaseReference;
    private Query mQuery;

    public FirebaseDbHelper(Context context) {
        listener = (DatabaseChangeListener) context;

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listener != null)
                    listener.onDataChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mQuery = mDatabaseReference.limitToFirst(MAX_NUM_ITEMS);
    }

    public Query getQuery() {
        return this.mQuery;
    }

    public void insert(@NotNull final LeaderboardItem entry) {
        mDatabaseReference
                .push()
                .setValue(entry, 0 - entry.getScore())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_TAG, String.format("Entry %s successfully added to %s", entry.toString(), TABLE_NAME));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(LOG_TAG, String.format("Failure to add entry %s to %s", entry.toString(), TABLE_NAME));
                    }
                });
    }

    public void update(@NotNull final LeaderboardItem entry, @NotNull String selectedKey) {
        mDatabaseReference.child(selectedKey)
                .setValue(entry, 0 - entry.getScore())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_TAG, String.format("Entry %s successfully updated in %s", entry.toString(), TABLE_NAME));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(LOG_TAG, String.format("Failure to update entry %s in %s", entry.toString(), TABLE_NAME));
                    }
                });
    }

    public void delete(@NotNull final String selectedKey) {
        mDatabaseReference.child(selectedKey)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_TAG, String.format("Key %s successfully deleted from %s", selectedKey, TABLE_NAME));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, String.format("Failure to delete key %s from %s", selectedKey, TABLE_NAME));
                    }
                });
    }

    public interface DatabaseChangeListener {
        void onDataChanged();
    }

}
