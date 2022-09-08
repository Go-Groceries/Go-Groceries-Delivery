package com.prasunpersonal.gogroceries_delivery;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.prasunpersonal.gogroceries_delivery.Models.ModelDeliveryGuy;

import java.text.DecimalFormat;

public class App extends Application {
    public static ModelDeliveryGuy ME;
    public static final String PROGRESS_NOTIFICATION_ID = "ProgressNotification";

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel progressChannel = new NotificationChannel(PROGRESS_NOTIFICATION_ID, "Ongoing Progress", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(progressChannel);
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
