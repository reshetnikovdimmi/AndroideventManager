package com.example.androidalarmmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.androidalarmmanager.DatabaseHelper.TABLE2;

public class ExampleReciver extends BroadcastReceiver {
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        AlarmManagerBroadcastReceiver BR = new AlarmManagerBroadcastReceiver();
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();
        db.delete(DatabaseHelper.TABLE2, "Date1 = ?", new String[]{String.valueOf(BR.calendar())});
               BR.names(context);
                Intent local = new Intent();
                local.setAction("service.to.activity.transfer");
                local.putExtra("number", "555");
                context.sendBroadcast(local);
        db.close();
    }

}
