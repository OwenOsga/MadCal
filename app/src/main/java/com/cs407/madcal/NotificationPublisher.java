package com.cs407.madcal;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION_TITLE = "notification-title";
    public static String NOTIFICATION_MESSAGE = "notification-message";

    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        Log.d("onReceive", "the value of int id is:" + id );
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String message = intent.getStringExtra(NOTIFICATION_MESSAGE);
        Log.d("onReceive", "the value of title is:" + title );
        Log.d("onReceive", "the value of message is:" + message );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.wisclogo);

        notificationManager.notify(id, builder.build());
    }
}
