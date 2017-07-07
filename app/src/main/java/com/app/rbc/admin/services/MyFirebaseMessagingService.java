package com.app.rbc.admin.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.Constant;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Rohit on 7/04/17.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        AppUtil.logger(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            AppUtil.logger(TAG, "Message data payload: " + remoteMessage.getData());
            if(remoteMessage.getData().get("type").equalsIgnoreCase("task_update")){
//
                try {
                    JSONArray array =  new JSONArray(remoteMessage.getData().get("data"));
                    JSONObject data = array.getJSONObject(0);
                    sendNotification(remoteMessage,data.getString("task_id"),remoteMessage.getData().get("title"),remoteMessage.getData().get("type"));


                    Intent pushNotification = new Intent(Constant.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type",remoteMessage.getData().get("type"));
                    pushNotification.putExtra("task_id", data.getString("task_id"));
                    pushNotification.putExtra("log_type", data.getString("log_type"));
                    pushNotification.putExtra("docs", data.getString("docs"));
                    pushNotification.putExtra("changed_by", data.getString("changed_by"));
                    pushNotification.putExtra("comment", data.getString("comment"));
                    pushNotification.putExtra("status", data.getString("status"));
                    pushNotification.putExtra("change_time",data.getString("change_time"));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if(remoteMessage.getData().get("type").equalsIgnoreCase("new_task"))
            {

                    sendNotification(remoteMessage, remoteMessage.getData().get("task_id"), remoteMessage.getData().get("title"), remoteMessage.getData().get("type"));


            }
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
               // scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            AppUtil.logger(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());




//            NotificationCompat.InboxStyle inboxStyle =
//                    new NotificationCompat.InboxStyle();
//            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Notification notification = new NotificationCompat.Builder(this)
//                    .setContentTitle(remoteMessage.getNotification().getTitle())
//                    .setContentText(remoteMessage.getNotification().getBody())
//                    .setSmallIcon(R.drawable.ic_ac_unit_black_24dp)
//                    .setColor(Color.parseColor("#FF4081"))
//                    .setSound(uri)
//                    .setStyle(inboxStyle)
//                    .build();
//
//
//            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
//
//
//            manager.notify(123, notification);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
//    private void scheduleJob() {
//        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
//        // [END dispatch_job]
//    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        AppUtil.logger(TAG, "Short lived task is done.");
    }

//    /**
//     * Create and show a simple notification containing the received FCM message.
//     *
//     * @param messageBody FCM message body received.
//     */
    private void sendNotification(RemoteMessage remoteMessage , String task_id ,String title , String task_type) {

        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("task_id",task_id);
        intent.putExtra("type",task_type);
        intent.putExtra("title",title);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this);

        final NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(builder);


        Notification notification = builder
                .setSmallIcon(R.drawable.ic_ac_unit_black_24dp)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setStyle(style.bigText(remoteMessage.getNotification().getBody()))

                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentText(remoteMessage.getNotification().getBody())
                .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(0x1234, notification);
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
