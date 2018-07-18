package com.example.lenovo.todolistactivitybased;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String todoName = intent.getStringExtra("todoTitle");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyChannelID", "name?", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MyChannelID");
        builder.setContentTitle("ToDo Alarm");
        builder.setContentText("ToDo: " + todoName);
        builder.setAutoCancel(true);

        builder.setSmallIcon(R.drawable.todo)
                .setDefaults(Notification.COLOR_DEFAULT)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.todo);
        builder.setLargeIcon(icon);

        //the following 3 lines of code let you open the mainActivity when u click the notification
        Intent intent1 = new Intent(context, MainActivity.class);
//        intent1.putExtras(intent);    //tried to pass intent via intent while trying to open correspondng display activity on notification click
//                                        so that correspondng task ka data display ho jaye, but intent variable mei vo sab data hai hi nahi :(
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent1, 0);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manager.notify(1, notification);

    }
}
