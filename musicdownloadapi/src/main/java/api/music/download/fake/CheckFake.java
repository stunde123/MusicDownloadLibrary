package api.music.download.fake;

import android.content.Context;
import android.text.TextUtils;


import api.music.download.BuildConfig;

public class CheckFake {

    public static boolean checkFake(Context context, String specialcountry, int level) {
        if (BuildConfig.DEBUG) {
            return false;
        }
        try {
            String phoneCountry = MDA_Utils.getPhoneCountry(context);
            String simCountry = MDA_Utils.getSimCountry(context);
            String language = MDA_Utils.getlanguage();


            if (!TextUtils.isEmpty(language) && language.equals("hi")) {
                return true;
            }

            if (checkCountry(context, specialcountry, simCountry)) {
                return true;
            }

            if (level == 0) {
                return false;
            }
            if (level == 1) {
                return MDA_Utils.isNotCommongUser(context);
            }
            if (MDA_Utils.isNotCommongUser(context)) {
                return true;
            }
            if (level == 2) {
                if (TextUtils.isEmpty(simCountry)) {
                    return false;
                }
                return checkCountry(context, specialcountry, simCountry);
            }

            if (TextUtils.isEmpty(phoneCountry) || TextUtils.isEmpty(simCountry)) {
                return true;
            }
            if (!phoneCountry.toLowerCase().equals(simCountry.toLowerCase())) {
                return true;
            }
            phoneCountry = phoneCountry.toLowerCase();
            return checkCountry(context, specialcountry, phoneCountry);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return true;
    }


    private static boolean checkCountry(Context context, String specialcountry, String country) {
        if (specialcountry.contains(country)) {
            return true;
        }
        String localCountry = MDA_Utils.getlocalCountry(context);
        if (!TextUtils.isEmpty(localCountry)) {
            if (specialcountry.contains(localCountry)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
