package com.ironxpert.admin.common.auth;

import com.ironxpert.admin.common.security.Preferences;
import com.ironxpert.admin.common.settings.TokenNames;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthPreferences {
    private SharedPreferences sharedPreferences;

    public AuthPreferences(Context context) {
        try {
            sharedPreferences = Preferences.getEncryptedSharedPreferences(context, "AUTH_TOKEN");
        } catch (Exception e) {
            e.printStackTrace();
            sharedPreferences = null;
        }
    }

    public void setAuthToken(String authToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TokenNames.AT, authToken);
        editor.apply();
    }

    public void setEncryptionKey(String encKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ENC_KEY", encKey);
        editor.apply();
    }

    public String getAuthToken() {
        if (sharedPreferences == null) return null;
        return sharedPreferences.getString(TokenNames.AT, null);
    }

    public String getEncryptionKey() {
        if (sharedPreferences == null) return null;
        return sharedPreferences.getString("ENC_KEY", null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
