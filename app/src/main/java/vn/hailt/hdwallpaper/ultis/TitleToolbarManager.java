package vn.hailt.hdwallpaper.ultis;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import vn.hailt.hdwallpaper.App;

public enum TitleToolbarManager {
    get;

    public String getTitle() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getApp());
        return pref.getString("title", "");
    }

    public void putTitle(String title) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getApp());
        pref.edit().putString("title", title).apply();
    }
}
