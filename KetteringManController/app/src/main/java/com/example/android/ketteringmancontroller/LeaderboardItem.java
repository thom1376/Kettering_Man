package com.example.android.ketteringmancontroller;

public class LeaderboardItem {
    private String mInitials;
    private int mScore;

    public LeaderboardItem() {

    }

    public LeaderboardItem(String initials, int score) {
        this.mInitials = initials;
        this.mScore = score;
    }

    public String getInitials() {
        return mInitials;
    }

    public void setInitials(String initials) {
        this.mInitials = initials;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        this.mScore = score;
    }
}
