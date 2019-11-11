package com.example.c196project.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static com.example.c196project.utilities.Const.ALARM_EXTRA;

public class WGUReceiver extends BroadcastReceiver
{
    static int notificationId = 1;
    private WGUNotificationMgr notificationMgr;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        notificationMgr = new WGUNotificationMgr();
        notificationMgr.initNotifications(context);
        Bundle extras = intent.getExtras();
        notificationMgr.sendNotification(notificationId, extras.getString(ALARM_EXTRA));
        notificationId++;
    }

}
