package com.example.notificationexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

public class MainActivity extends AppCompatActivity {
    public final String CHANNEL_ID = "Personal Notification";
    public static final int NOTIFICATION_ID = 101;
    public static final String txtReply = "Text_Reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void NotificationDisplay(View view) {
        Intent landingintent = new Intent(this, LandingActivity.class);
        landingintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(this, 0,
                landingintent, PendingIntent.FLAG_ONE_SHOT);

        Intent AnswerIntent = new Intent(this, YesA.class);
        AnswerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent ReplyPendingIntent = PendingIntent.getActivity(this, 0, AnswerIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent DismissIntent = new Intent(this, NoA.class);
        DismissIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent DismissPendingIntent = PendingIntent.getActivity(this, 0, DismissIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_message_notification);
        builder.setContentTitle("Simple Notification");
        builder.setContentText("This is a simple notification");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);

        builder.setContentIntent(landingPendingIntent);
        // Action Button to a Notification
        builder.addAction(R.drawable.ic_message_notification, "ANSWER", ReplyPendingIntent);
        builder.addAction(R.drawable.ic_message_notification, "DISMISS ", DismissPendingIntent);

        // Direct Reply Action to a Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            RemoteInput remoteInput = new RemoteInput.Builder(txtReply).setLabel("Reply").build();

            Intent replyIntent = new Intent(this, RemoteReceiver.class);
            replyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent replyPendingIntent = PendingIntent.getActivity(this, 0, replyIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Action action=new NotificationCompat.Action.Builder(R.drawable.ic_message_notification,
                    "Reply", replyPendingIntent).addRemoteInput(remoteInput).build();

            builder.addAction(action);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
        //Notification with Progress Bar
    public void DisplayDownLoad(View view) {
        CreatenotificationChannel();
      final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_dowload);
        builder.setContentTitle("Image Download");
        builder.setContentText("DownLoad in progress");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

     final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
       final int max_progress = 100;
        int current_progress = 0;
        builder.setProgress(max_progress, current_progress, false);

        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        Thread thread = new Thread()
        {
            @Override
            public void run() {
            int count = 0;
            try {
                while (count <= 100) {
                    count = count + 10;
                    sleep(1000);
                 builder.setProgress(max_progress,count, false);
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                }
                builder.setContentText("Download Complete");
                builder.setProgress(0, 0, false);
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

            } catch (InterruptedException e) {}
            }
        };
        thread.start();
    }
        //Creating Notification Channel
     private void CreatenotificationChannel () {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                CharSequence name = "Personal Notification";
                String description = "Include all personal Notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                notificationChannel.setDescription(description);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);

            }
        }

}