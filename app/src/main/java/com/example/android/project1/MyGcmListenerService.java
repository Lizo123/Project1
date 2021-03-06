package com.example.android.project1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Joey on 11/9/2015.
 */
public class MyGcmListenerService extends GcmListenerService {

    public MyGcmListenerService() {
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Get the data  that was sent from the GCM server
        String message = data.getString("message");
        String title = data.getString("title");

        Log.d("MyGcmListenerService", "From: " + from);
        Log.d("MyGcmListenerService", "Message: " + message);

        if (from.startsWith("/topics/")) {
            //message received from some topic.
        } else {
            //normal downstream message.
        }

        //Pass the message that was received to the ListView
        addToListView(message);
        //Store the message that was received in a local SQLite database

        //Show a notification
        sendNotification(title,message);
    }

    private void addToListView(String message)
    {
        Intent intent = new Intent("newMessageIntent");
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(MyGcmListenerService.this).sendBroadcast(intent);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, ChatPageWithListView.class);
        intent.putExtra("message", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.powered_by_google_dark)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
