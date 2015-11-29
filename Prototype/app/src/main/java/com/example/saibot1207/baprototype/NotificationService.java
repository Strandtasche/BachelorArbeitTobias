package com.example.saibot1207.baprototype;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.service.notification.NotificationListenerService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.support.v4.content.LocalBroadcastManager;

import java.sql.SQLException;


/**
 * Created by saibot1207 on 23.10.15.
 */
public class NotificationService extends NotificationListenerService {

    Context context;
    private MySQLiteHelper dbHelper;
    private SQLiteDatabase database;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NOTIFICATIONENTRY};

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        dbHelper = new MySQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d("NotificationEntry", "NotificationEntry posted");

        String pack = sbn.getPackageName();
        String ticker = sbn.getNotification().tickerText.toString();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        Log.i("Package", pack);
        Log.i("Ticker", ticker);
        Log.i("Title", title);
        Log.i("Text", text);

        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);



        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NOTIFICATIONENTRY, pack);
        values.put(MySQLiteHelper.COLUMN_TITLEHASHED, title);
        long insertId = database.insert(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        cursor.close();
        Log.d("db not", "Updated database");


        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d("Msg", "NotificationEntry Removed");
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public void onDestroy() {
        close();
        super.onDestroy();
    }

    private int hashString (String str) {
        int i = 1;
        int sum = 1;
        for( char s : str.toCharArray()) {
            sum += i * (int) s;
            i++;
            if (i > 20) {
                break;
            }
        }
        return sum;
    }

}
