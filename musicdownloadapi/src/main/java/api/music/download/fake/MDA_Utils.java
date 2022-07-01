package api.music.download.fake;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.util.Locale;

import api.music.download.BuildConfig;

public class MDA_Utils {

    public static String getPhoneCountry(Context context) {
        String country = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.getPhoneType()
                    != TelephonyManager.PHONE_TYPE_CDMA) {
                country = telephonyManager.getNetworkCountryIso();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return country;
    }

    public static String getSimCountry(Context context) {
        String country = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simCountry = telephonyManager.getSimCountryIso();
            if (!TextUtils.isEmpty(simCountry)) {
                country = simCountry.toLowerCase();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return country;
    }

    public static String getlanguage() {
        try {
            return Locale.getDefault().getLanguage();
        } catch (Throwable e) {
        }
        return "";
    }

    public static boolean isNotCommongUser(Context context) {
        if (BuildConfig.DEBUG) {
            return false;
        }
        return isAdbDebugEnable(context) || isEmulator() || isRoot();
    }

    @TargetApi(3)
    public static boolean isAdbDebugEnable(Context context) {
        boolean enableAdb = (Settings.Secure.getInt(
                context.getContentResolver(), Settings.Global.ADB_ENABLED, 0) > 0);
        return enableAdb;
    }

    private static boolean isEmulator() {
        try {
            return Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.toLowerCase().contains("vbox")
                    || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                    || "google_sdk".equals(Build.PRODUCT);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isRoot() {
        boolean bool = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bool;
    }

    public static String getlocalCountry(Context context) {
        try {
            return context.getResources().getConfiguration().locale.getCountry();
        } catch (Throwable e) {
        }
        return "";
    }
}
