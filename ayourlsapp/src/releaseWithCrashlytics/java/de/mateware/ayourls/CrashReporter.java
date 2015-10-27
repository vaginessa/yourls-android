package de.mateware.ayourls;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mate on 08.10.2015.
 */
public class CrashReporter {
    public static void init(Context context) {
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG)
                                                            .build();
        Fabric.with(context, new Crashlytics.Builder().core(core)
                                                      .build());
    }

    public static void addCustomKey (String key, Object value) {
        if (value instanceof String)
            Crashlytics.getInstance().core.setString(key, (String) value);
        else if (value instanceof Integer) {
            Crashlytics.getInstance().core.setInt(key, (Integer) value);
        }else if (value instanceof Boolean) {
            Crashlytics.getInstance().core.setBool(key, (Boolean) value);
        }else if (value instanceof Double) {
            Crashlytics.getInstance().core.setDouble(key, (Double) value);
        }else if (value instanceof Float) {
            Crashlytics.getInstance().core.setFloat(key, (Float) value);
        }else if (value instanceof Long) {
            Crashlytics.getInstance().core.setLong(key, (Long) value);
        } else
            Crashlytics.getInstance().core.setString(key, value.toString());
    }

    public static void setUserEmail(String userEmail) {
        Crashlytics.getInstance().core.setUserEmail(userEmail);
    }
    public static void setUserName(String userName) {
        Crashlytics.getInstance().core.setUserName(userName);
    }
    public static void setUserIdentifier(String userIdentifier) {
        Crashlytics.getInstance().core.setUserIdentifier(userIdentifier);
    }

}
