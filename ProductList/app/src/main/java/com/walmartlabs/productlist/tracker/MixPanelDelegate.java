package com.walmartlabs.productlist.tracker;

import android.content.Context;
import android.provider.Settings;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.dao.SharedPreferencesHelper;
import com.walmartlabs.productlist.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MixPanelDelegate {

    private static MixpanelAPI sMixpanel;
    private static boolean sIsEnabled = false;
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
        final String mixPanelToken = context.getString(R.string.mix_panel_token);
        sMixpanel = MixpanelAPI.getInstance(sContext, mixPanelToken);
        sIsEnabled = sMixpanel != null;

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        if (Boolean.parseBoolean(sharedPreferencesHelper.read(SharedPreferencesHelper.Key.FIRST_RUN))) {
            sharedPreferencesHelper.write(SharedPreferencesHelper.Key.FIRST_RUN, "false");
            if (sIsEnabled) {
                JSONObject properties = new JSONObject();
                try {
                    //To track the user for this sample app I am using the Android ID
                    //Ideally, we would be generating an ID in our server or using a combination of attributes
                    properties.put(Constants.MIX_PANEL_USER_ID, getUserAndroidId());
                } catch (JSONException e) {
                    //In the future we would be logging this in Crashlytics
                }
                sMixpanel.registerSuperPropertiesOnce(properties);
                track(Constants.MIX_PANEL_INSTALL_APP_EVENT, null);
            }
        }
    }

    public static void track(String eventName, Map<String, String> params) {
        if (sIsEnabled) {
            try {
                JSONObject properties = null;
                if (params != null) {
                    properties = new JSONObject(params);
                }
                sMixpanel.track(eventName, properties);
            } catch (Exception e) {
                //In the future we would be logging this in Crashlytics
            }
        }
    }

    public static void flush() {
        if (sIsEnabled) {
            sMixpanel.flush();
        }
    }

    private static String getUserAndroidId() {
        return Settings.Secure.getString(sContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
