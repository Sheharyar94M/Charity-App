package com.example.ultimatebook.cloud_notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.charity.R;
import com.example.ultimatebook.i_want.ui.home.HomeWantActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

@SuppressLint({"MissingFirebaseInstanceTokenRefresh","UnspecifiedImmutableFlag"})
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(this,notification);

        ringtone.play();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){

            ringtone.setLooping(false);
        }

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100,300,300,300};
        vibrator.vibrate(pattern,-1);

//        int resourceImage = getResources().getIdentifier(Objects.requireNonNull(message.getNotification()).getIcon(),"drawable",getPackageCodePath());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"com.example.haqddarcherity");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder.setSmallIcon(R.mipmap.app_icon);
        else
            builder.setSmallIcon(R.mipmap.app_icon);

        Intent resultIntent = new Intent(this, HomeWantActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle(Objects.requireNonNull(message.getNotification()).getTitle());
        builder.setContentTitle(message.getNotification().getBody());
        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager mnotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelId = "com.example.haqddarcherity";
            NotificationChannel channel = new NotificationChannel(channelId,"Help someone!",NotificationManager.IMPORTANCE_HIGH);
            mnotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        mnotificationManager.notify(100,builder.build());
    }
}
