package com.walmartlabs.productlist.tracker;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.dao.SharedPreferencesHelper;

public class MixPanelDelegate {

    private static MixpanelAPI sMixpanel;
    private static boolean sIsEnabled = false;
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
        final String mixPanelToken = context.getString(R.string.mix_panel_token);
        sMixpanel = MixpanelAPI.getInstance(sContext.getApplicationContext(), mixPanelToken);
        sIsEnabled = sMixpanel != null;

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        if (Boolean.parseBoolean(sharedPreferencesHelper.read(SharedPreferencesHelper.Key.FIRST_RUN))) {
            sharedPreferencesHelper.write(SharedPreferencesHelper.Key.FIRST_RUN, "true");
            if (sIsEnabled) {
                sMixpanel.track("App Install", null);

            }

            updatePeopleProperty("HermesId", "");
        }
    }

    public static void flush() {
        if (sIsEnabled) {
            sMixpanel.flush();
        }
    }

}
