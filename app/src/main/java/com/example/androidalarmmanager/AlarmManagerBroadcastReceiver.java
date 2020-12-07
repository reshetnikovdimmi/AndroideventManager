package com.example.androidalarmmanager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.androidalarmmanager.DatabaseHelper.TABLE2;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";
    private static String CHANNEL_ID = "Cat channel";
    int dencal,mes,god;
    private static final int NOTIFY_ID = 101;
    SQLiteDatabase db;
    Cursor userCursor;
    DatabaseHelper databaseHelper;
    String name,date;
    long time;
    public Object denznah ;
User user;
    NotificationManagerCompat notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here.
        Bundle extras = intent.getExtras();

        StringBuilder msgStr = new StringBuilder();

        if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
            //Make sure this intent has been sent by the one-time timer button.
            msgStr.append("One time Timer : ");
        }
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));
        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
        Log.d("summD1", calendar() + "календарь");


        names(context);
        //Release the lock

        wl.release();
    }
    public String calendar () {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);

        dencal = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH) + 1;
        god = calendar.get(Calendar.YEAR);
        time = calendar.getTimeInMillis();
        String date1 = god + "." + mes + "." + dencal;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
        Date datenorm;
        String data = null;
        try {
            datenorm = dateFormat.parse(date1);
            data = dateFormat.format(datenorm.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("summD1", data);

        return data;
    }
    public void   names (Context context) {

        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE2 + " where " +
                DatabaseHelper.COLUMN_YEAR2 + "=?", new String[]{calendar()});
        userCursor.moveToFirst();
       // Cursor cursor = db.query(TABLE2, null, null, null, null, null, null);
       // Log.d("updatedCursor", String.valueOf(cursor + "+1"));
       // AlarmManagerActivity AM = new AlarmManagerActivity();
        //AM.updateListView(context);

        if( userCursor != null && userCursor.moveToFirst() ) {
            date = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR2));

            if (date.equals(calendar())) {

                name = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME2));
hashmap2();
               notif(context,name);
            }
            } else {
Log.d("calendar",calendar());
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                        DatabaseHelper.COLUMN_YEAR + "=?", new String[]{calendar()});
                userCursor.moveToFirst();

                name = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                hashmap();
                notif(context,name);
                date=null;
            }



    }
    public Object hashmap () {
        Map<Integer, Object> den = new HashMap<>();
        den.put(1, R.drawable.icon_1);
        den.put(2, R.drawable.icon_2);
        den.put(3, R.drawable.icon_3);
        den.put(4, R.drawable.icon_4);
        den.put(5, R.drawable.icon_5);
        den.put(6, R.drawable.icon_6);
        den.put(7, R.drawable.icon_7);
        den.put(8, R.drawable.icon_8);
        den.put(9, R.drawable.icon_9);
        den.put(10, R.drawable.icon_10);
        den.put(11, R.drawable.icon_11);
        den.put(12, R.drawable.icon_12);
        den.put(13, R.drawable.icon_13);
        den.put(14, R.drawable.icon_14);
        den.put(15, R.drawable.icon_15);
        den.put(16, R.drawable.icon_16);
        den.put(17, R.drawable.icon_17);
        den.put(18, R.drawable.icon_18);
        den.put(19, R.drawable.icon_19);
        den.put(20, R.drawable.icon_20);
        den.put(21, R.drawable.icon_21);
        den.put(22, R.drawable.icon_22);
        den.put(23, R.drawable.icon_23);
        den.put(24, R.drawable.icon_24);
        den.put(25, R.drawable.icon_25);
        den.put(26, R.drawable.icon_26);
        den.put(27, R.drawable.icon_27);
        den.put(28, R.drawable.icon_28);
        den.put(29, R.drawable.icon_29);
        den.put(30, R.drawable.icon_30);
        den.put(31, R.drawable.icon_31);

        calendar();

        denznah = den.get(dencal);
user = new User(denznah);
        Log.d("dencal", String.valueOf(dencal) + "-1");
        return denznah;
    }

    public Object hashmap2 () {
        Map<Integer, Object> den = new HashMap<>();
        den.put(1, R.drawable.icon_2_1);
        den.put(2, R.drawable.icon_2_2);
        den.put(3, R.drawable.icon_2_3);
        den.put(4, R.drawable.icon_2_4);
        den.put(5, R.drawable.icon_2_5);
        den.put(6, R.drawable.icon_2_6);
        den.put(7, R.drawable.icon_2_7);
        den.put(8, R.drawable.icon_2_8);
        den.put(9, R.drawable.icon_2_9);
        den.put(10, R.drawable.icon_2_10);
        den.put(11, R.drawable.icon_2_11);
        den.put(12, R.drawable.icon_2_12);
        den.put(13, R.drawable.icon_2_13);
        den.put(14, R.drawable.icon_2_14);
        den.put(15, R.drawable.icon_2_15);
        den.put(16, R.drawable.icon_2_16);
        den.put(17, R.drawable.icon_2_17);
        den.put(18, R.drawable.icon_2_18);
        den.put(19, R.drawable.icon_2_19);
        den.put(20, R.drawable.icon_2_20);
        den.put(21, R.drawable.icon_2_21);
        den.put(22, R.drawable.icon_2_22);
        den.put(23, R.drawable.icon_2_23);
        den.put(24, R.drawable.icon_2_24);
        den.put(25, R.drawable.icon_2_25);
        den.put(26, R.drawable.icon_2_26);
        den.put(27, R.drawable.icon_2_27);
        den.put(28, R.drawable.icon_2_28);
        den.put(29, R.drawable.icon_2_29);
        den.put(30, R.drawable.icon_2_30);
        den.put(31, R.drawable.icon_2_31);

        calendar();

        Object denznah = den.get(dencal);
        user = new User(denznah);

        Log.d("dencal", String.valueOf(user.getDenznah())+"-4");
        return denznah;
    }
    public void notif (Context context,String name) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My channel",
                    NotificationManager.IMPORTANCE_HIGH);
           // channel.setDescription("My channel description");
           // channel.enableLights(true);
           // channel.setLightColor(Color.RED);
            //channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }
        Intent notificationIntent = new Intent(context, AlarmManagerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews remoteViews = new RemoteViews(null, R.layout.notification);
        remoteViews.setTextViewText(R.id.textView, name);
        remoteViews.setImageViewResource(R.id.View, R.drawable.ic_icon_foreground);

        remoteViews.setOnClickPendingIntent(R.id.root, contentIntent);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        builder.setSmallIcon((Integer) user.getDenznah());
        builder.setColorized(true);
        builder.setDefaults(0);
        builder.setCategory(Notification.CATEGORY_SERVICE);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentText(name);

        builder.setContentIntent(contentIntent);
       if (date!=null && date.equals(calendar())) {
            Intent intent = new Intent(context,ExampleReciver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
            builder.addAction(R.drawable.ic_icon_foreground, "Выполниить",pIntent);
        }
      // builder.setCustomContentView(remoteViews);

//builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        builder.setOngoing(true);

       // notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());

        if (sPref.getBoolean("firstrun", false) == false && date == null) {

            notificationManager.cancelAll();
        } else {

            date = null;
        }

    }
public void cancelAll(){
    notificationManager.cancelAll();
}
    public void SetAlarm(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 5 seconds

        calendar();
        am.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_HOUR, pi);
        }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}