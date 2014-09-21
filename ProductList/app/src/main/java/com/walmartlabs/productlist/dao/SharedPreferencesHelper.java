package com.walmartlabs.productlist.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.util.Constants;

public class SharedPreferencesHelper {

    private static SharedPreferencesHelper sharedPreferencesHelper;
    private static SharedPreferences sharedPreferences;

    private SharedPreferencesHelper() {
    }

    public static SharedPreferencesHelper getInstance(Context context) {
        if(sharedPreferencesHelper == null) {
            sharedPreferences = context.getSharedPreferences(
                    context.getResources().getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
            sharedPreferencesHelper = new SharedPreferencesHelper();
        }
        return sharedPreferencesHelper;
    }

    //Accessor methods
    public interface DefaultKey {
        public String getDefaultValue();

        public String name();
    }

    public enum Key implements DefaultKey {
        LAST_DATA_ACCESS_TIME(0l);

        private String defaultStringValue;
        private Long defaultLongValue;

        private Key(String defaultValue) {
            this.defaultStringValue = defaultValue;
        }

        private Key(Long defaultValue) {
            this.defaultLongValue = defaultValue;
        }

        @Override
        public String getDefaultValue() {
            return defaultStringValue;
        }
    }

    //Write
    public void write(Key key, String value) {
        write(key.name(), value);
    }

    private void write(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void writeLong(Key key, Long value) {
        writeLong(key.name(), value);
    }

    private void writeLong(String key, Long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    //Read
    public String read(Key key) {
        return read(key.name(), key.defaultStringValue);
    }

    private String read(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public Long readLong(Key key) {
        return readLong(key.name(), key.defaultLongValue);
    }

    private Long readLong(String key, Long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }
}
