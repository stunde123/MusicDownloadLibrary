package api.music.download.fragment;

import android.annotation.SuppressLint;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timeutils {
    public static void writeToFile(String fileName, String content) {
        try {
            writeToFile(fileName, content.getBytes("UTF-8"));
        } catch (Exception e) {

        }
    }

    public static void writeToFile(String fileName, byte[] content) {
        File f = new File(fileName);

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {

            }
        }

        try {
            FileOutputStream opt = new FileOutputStream(f, false);
            opt.write(content, 0, content.length);
            opt.close();
        } catch (Exception e) {

        }
    }

    public static String readFromFile(String file) {
        try {
            File f = new File(file);

            if (!f.exists() || !f.canRead()) {
                return null;
            }

            BufferedInputStream ipt = new BufferedInputStream(new FileInputStream(f));

            byte[] buf = new byte[512];
            StringBuilder sb = new StringBuilder();

            while (ipt.available() > 0) {
                int len = ipt.read(buf, 0, 512);
                sb.append(new String(buf, 0, len, "UTF-8"));
            }

            ipt.close();
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return String.format("%d B", bytes);
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f kB", (float) bytes / 1024);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", (float) bytes / 1024 / 1024);
        } else {
            return String.format("%.2f GB", (float) bytes / 1024 / 1024 / 1024);
        }
    }

    public static String formatSpeed(float speed) {
        if (speed < 1024) {
            return String.format("%.2f B/s", speed);
        } else if (speed < 1024 * 1024) {
            return String.format("%.2f kB/s", speed / 1024);
        } else if (speed < 1024 * 1024 * 1024) {
            return String.format("%.2f MB/s", speed / 1024 / 1024);
        } else {
            return String.format("%.2f GB/s", speed / 1024 / 1024 / 1024);
        }
    }

    public static String secToTime(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String getFileExt(String url) {
        int index;
        if ((index = url.indexOf("?")) > -1) {
            url = url.substring(0, index);
        }

        index = url.lastIndexOf(".");
        if (index == -1) {
            return null;
        } else {
            String ext = url.substring(index);
            if ((index = ext.indexOf("%")) > -1) {
                ext = ext.substring(0, index);
            }
            if ((index = ext.indexOf("/")) > -1) {
                ext = ext.substring(0, index);
            }
            return ext.toLowerCase();
        }
    }

    @SuppressLint("DefaultLocale")
    public static String formatDuration(int duration) {
        duration /= 1000; // milliseconds into seconds
        int minute = duration / 60;
        int hour = minute / 60;
        minute %= 60;
        int second = duration % 60;
        if (hour != 0)
            return String.format("%2d:%02d:%02d", hour, minute, second);
        else
            return String.format("%02d:%02d", minute, second);
    }

    public static String getDateStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
