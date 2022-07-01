package api.music.download.util;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public
class Utils {
    public static String jointParams(String url, Map<String, String> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    public static Class<?> analysisInterfaceClazzInfo(Object object) {
        Type genType = object.getClass().getGenericInterfaces()[0];
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        return (Class<?>) params[0];
    }

    public static Type analysisInterfaceTypeInfo(Object object) {
        Type genType = object.getClass().getGenericInterfaces()[0];
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return params[0];
    }
}
