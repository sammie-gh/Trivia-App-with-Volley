package com.gh.sammie.parasingapi.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighScore(int score) {
        int currentScore = score;
        int lastScore = preferences.getInt("high_score", 0);

        if (currentScore > lastScore) {
            // weh ave new hig score
            preferences.edit().putInt("high_score", currentScore).apply();
        }

    }

    public int getHighScore() {
        return preferences.getInt("high_score", 0);
    }

    public  void setState(int index) {
        preferences.edit().putInt("index_state", index).apply();
    }

    public int getState() {
        return preferences.getInt("index_state", 0);
    }
}
