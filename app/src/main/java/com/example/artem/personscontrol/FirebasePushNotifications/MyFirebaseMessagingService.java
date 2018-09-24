package com.example.artem.personscontrol.FirebasePushNotifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.NavigationActivity;
import com.example.artem.personscontrol.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    static String CHANNEL_ID = "percontrol_channel_id";

    @Override
    public void onNewToken(String fcm_token) {
        super.onNewToken(fcm_token);

        Data_Singleton.deviceFCMToken = fcm_token;
        Log.d("FirebaseMessaging", "onNewToken: " + Data_Singleton.deviceFCMToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d("MsessageFirebseInfo", "From: " + remoteMessage.getFrom() + " | Type: " +  remoteMessage.getMessageType());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("MsessageFirebseData", "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

            // Загрузка иконок и картинок
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Bitmap enotik = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_send);

            // Создание уведомления
            Context context = getApplicationContext();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, "notify_001")
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setContentText(remoteMessage.getData().get("message"))
                            .setSmallIcon(R.drawable.ic_menu_camera)
                            .setLargeIcon(enotik);

            Notification notification = mBuilder.build();

            // Загрузка пользовательского шаблона уведомления
            //RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notify);

            //contentView.setImageViewResource(R.id.image, R.drawable.ic_launcher);
            //contentView.setTextViewText(R.id.text,"Привет, мир!!!");

            // Установка пользовательского шаблона уведомления
            //notification.contentView = contentView;

            // Настройка перехода из уведомления в активность
            Intent notificationIntent = new Intent(this, NavigationActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            notification.contentIntent = contentIntent;

            // Играть музычку
            notification.defaults |= Notification.DEFAULT_SOUND;

            // Вибрировать
            notification.defaults |= Notification.DEFAULT_VIBRATE;

            // Мигать огоньками
            notification.defaults |= Notification.DEFAULT_LIGHTS;

            // 1 параметр - время до запуска вибрации
            // 2,3,4 - времена вибраций (может быть много чисел)
            long[] vibrate = {0,100,200,300};
            notification.vibrate = vibrate;

            // цвет для RGB индикатора.
            notification.ledARGB = 0xff00ff00;

            // время между миганиями
            notification.ledOnMS = 300;

            // время, через которое индикатор потухнет
            notification.ledOffMS = 1000;

            // включить мигание
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("notify_001",
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(1, notification);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            // Загрузка иконок и картинок
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Bitmap enotik = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_send);

            // Создание уведомления
            Context context = getApplicationContext();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, "notify_001")
                            .setContentTitle("Custom уведомление")
                            .setContentText("Текст уведомления")
                            .setSmallIcon(R.drawable.ic_menu_camera)
                            .setLargeIcon(enotik);

            Notification notification = mBuilder.build();

            // Загрузка пользовательского шаблона уведомления
            //RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notify);

            //contentView.setImageViewResource(R.id.image, R.drawable.ic_launcher);
            //contentView.setTextViewText(R.id.text,"Привет, мир!!!");

            // Установка пользовательского шаблона уведомления
            //notification.contentView = contentView;

            // Настройка перехода из уведомления в активность
            Intent notificationIntent = new Intent(this, NavigationActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            notification.contentIntent = contentIntent;

            // Играть музычку
            notification.defaults |= Notification.DEFAULT_SOUND;

            // Вибрировать
            notification.defaults |= Notification.DEFAULT_VIBRATE;

            // Мигать огоньками
            notification.defaults |= Notification.DEFAULT_LIGHTS;

            // 1 параметр - время до запуска вибрации
            // 2,3,4 - времена вибраций (может быть много чисел)
            long[] vibrate = {0,100,200,300};
            notification.vibrate = vibrate;

            // цвет для RGB индикатора.
            notification.ledARGB = 0xff00ff00;

            // время между миганиями
            notification.ledOnMS = 300;

            // время, через которое индикатор потухнет
            notification.ledOffMS = 1000;

            // включить мигание
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("notify_001",
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(1, notification);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        //Toast.makeText(this.getBaseContext(), "FB Notification was get", Toast.LENGTH_LONG).show();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
