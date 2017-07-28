package com.app.rbc.admin.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.utils.AppUtil;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rohit on 27/7/17.
 */

public class DeadlineNotificationService extends BroadcastReceiver {

   // final public static String ONE_TIME = "onetime";
    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
       // Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        sendNotification(context,intent.getStringExtra("task_id"),intent.getStringExtra("title"),intent.getStringExtra("task_type"));

    }

    public void setOnetimeTimer(Context context ,Date date, String task_id , String title , String task_type, int broadcast_id){
       Calendar calendar =Calendar.getInstance();
        calendar.setTime(date);
        //calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)-1);
        AppUtil.logger("One time alarm : ","Calendar : "+calendar+" Task : "+title);

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DeadlineNotificationService.class);
        intent.putExtra("task_id",task_id);
        intent.putExtra("title",title);
        intent.putExtra("task_type",task_type);
       // intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, broadcast_id, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
    }


    private void sendNotification(Context context , String task_id , String title , String task_type) {

        int unique_id = Integer.parseInt(task_id.substring(task_id.indexOf('_')+1));
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("task_id",task_id);
        intent.putExtra("type",task_type);
        intent.putExtra("title",title);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,unique_id /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);

        final NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(builder);


        Notification notification = builder
                .setSmallIcon(R.drawable.ic_ac_unit_black_24dp)
                .setContentTitle("Deadline Approaching")
                .setStyle(style.bigText("Please make sure you complete the task before deadline \n Task : "+title))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentText("Please make sure you complete the task before deadline \n Task :" + title)
                .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        notificationManager.notify(unique_id, notification);
//
//        NotificationCompat.BigTextStyle inboxStyle =
//                new NotificationCompat.BigTextStyle();

//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_ac_unit_black_24dp)
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody())
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setStyle(inboxStyle)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}