package com.example.c196project.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.core.app.NotificationCompat;

public class WGUNotificationMgr
{

    private NotificationManager notificationManager;
    private Context context;
    private static String channelId = "testChannel";

    public void initNotifications(Context context)
    {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        initNotifyChannel(
                channelId,
                "Test WGUNotificationMgr",
                "Sample text for description"
        );
    }

    private void initNotifyChannel(String id, String title, String description)
    {
        NotificationChannel channel =
                new NotificationChannel(id, title, NotificationManager.IMPORTANCE_LOW);

        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.enableVibration(true);
        channel.setVibrationPattern(
                new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotification(View view)
    {
        String message = "Oh no! Your barbarians are calling the hog riders the N-word! Also, Ben is awesome!";
        Notification notification =
                new Notification.Builder(context,
                        channelId)
                        .setStyle(new Notification.BigTextStyle().bigText(message))
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setChannelId(channelId)
                        .build();
        notificationManager.notify(100, notification);
    }


}
