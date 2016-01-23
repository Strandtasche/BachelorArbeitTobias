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

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;


/**
 * Created by saibot1207 on 23.10.15.
 */
public class NotificationService extends NotificationListenerService {

    Context context;
    private MySQLiteHelper dbHelper;
    private SQLiteDatabase database;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NOTIFICATIONENTRY};

    private HashSet<String> validPackages;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        dbHelper = new MySQLiteHelper(context);
        database = dbHelper.getWritableDatabase();

        String[] temp = context.getResources().getStringArray(R.array.package_array);
        validPackages = new HashSet<>(Arrays.asList(temp));
        //Log.d("did that word?", temp[0]);
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

//        Intent msgrcv = new Intent("Msg");
//        msgrcv.putExtra("package", pack);
//        msgrcv.putExtra("ticker", ticker);
//        msgrcv.putExtra("title", title);
//        msgrcv.putExtra("text", text);

        int hashedTitle = hashString(title);
        int textSize = text.length();

        if (validPackages.contains(pack)) {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_NOTIFICATIONENTRY, pack);
            values.put(MySQLiteHelper.COLUMN_TITLEHASHED, Integer.toString(hashedTitle));
            values.put(MySQLiteHelper.COLUMN_TEXTLENGTH, Integer.toString(textSize));
            values.put(MySQLiteHelper.COLUMN_DATE, System.currentTimeMillis());
            long insertId = database.insert(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES, null,
                    values);
            Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES,
                    allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            cursor.close();
            Log.d("db not", "Updated database");
        }

        //LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
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
