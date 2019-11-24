package com.example.c196project.utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import static com.example.c196project.utilities.Const.ALARM_EXTRA;

public class WGUNotificationMgr
{

    private NotificationManager notificationManager;
    private Context context;
    private static String channelId = "testChannel";

    public void initNotifications(Context context)
    {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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

    public void sendNotification(int notificationId, String message)
    {
        Notification notification =
                new Notification.Builder(context,
                        channelId)
                        .setStyle(new Notification.BigTextStyle().bigText(message))
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setChannelId(channelId)
                        .build();
        Log.i("MethodCalled", "Notificaiton ID set: " + notificationId);
        notificationManager.notify(notificationId, notification);
    }

    public void setAlarm(Date date, String extraText, Context context)
    {
        Intent receiverIntent = new Intent(context, WGUReceiver.class);
        receiverIntent.putExtra(ALARM_EXTRA, extraText);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, receiverIntent, 0);
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar due = Calendar.getInstance();
        due.setTime(date);
        // Don't keep track of hrs / mins
        due.set(Calendar.HOUR_OF_DAY, 0);
        due.set(Calendar.MINUTE, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, due.getTimeInMillis(), sender);
        Log.i("MethodCalled", "An alarm should now be set for " + due);
    }


}
