/*
 * Copyright (C) 2014 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.saibot1207.baprototype;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saibot1207.baprototype.logger.Log;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    TableLayout tab;
    TextView textView;
    NotificationManager mNotifyMgr;
    Location mLastLocation;

    private MySQLiteHelper dbHelper;
    private SQLiteDatabase database;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NOTIFICATIONENTRY};

    private Context context;

    private Intent intentService;

    private CallData callData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        intentService = new Intent(this, NotificationService.class);
        startService(intentService);

        context = getApplicationContext();

        tab = (TableLayout) findViewById(R.id.tab);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        dbHelper = new MySQLiteHelper(this);

        callData = new CallData();

        Log.d("onCreate", "starting up!");
    }





    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");


            TableRow tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml(pack + "<br><b>" + title + " : </b>" + text));
            tr.addView(textview);
            tab.addView(tr);


        }
    };

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        close();
        super.onPause();
    }

    @Override
    protected void onResume() {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        stopService(intentService);
        super.onDestroy();
    }


    public void checkPermissions(View v) {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }

    public void createNotification(View v) {
        Notification notification = new Notification();
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setTicker("Ticker Text");
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int notificationID = 0;
        mNotifyMgr.notify(notificationID, mBuilder.build());
    }

    public void createNewActivity(View v) {
        Intent intent = new Intent(this, TestDatabaseActivity.class);
        startActivity(intent);
    }



    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public void addStuff(View v) {
        NotificationEntry notificationEntry;
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NOTIFICATIONENTRY, "this is a test");
        values.put(MySQLiteHelper.COLUMN_TITLEHASHED, "titlehashed");
        values.put(MySQLiteHelper.COLUMN_TEXTLENGTH, "not a number");
        values.put(MySQLiteHelper.COLUMN_DATE, System.currentTimeMillis());
        long insertId = database.insert(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        //NotificationEntry newNotificationEntry = cursorToNotificationEntry(cursor);
        cursor.close();
        //adapter.add(notificationEntry); n
    }

    private void getCallDetails() {

        //StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);

        int amount = 0;
        int amountOutgoing = 0;
        int amountIncoming = 0;
        int amountMissed = 0;



        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE); // Milliseconds since epoch.
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        int totalDuration = 0;

        for (int i = 0; i < 3; i++) {
            managedCursor.moveToNext();
        //while (managedCursor.moveToNext()) { // && ungetestet! TODO!
            amount++;
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            //Log.d("getCallDetails", "test!");
            System.out.println("is this working?");
            totalDuration += Integer.parseInt(callDuration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    amountOutgoing++;
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    amountIncoming++;
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    amountMissed++;
                    break;
            }
        }
        //Log.d("getCalldetails", "before close");
        managedCursor.close();
        callData.setAmountIncoming(amountIncoming);
        callData.setAmountMissed(amountMissed);
        callData.setAmountOutgoing(amountOutgoing);
        callData.setTotalDuration(totalDuration);
        //Log.d("getCalldetail", "data set");
        return;

    }

    private void getMessageDetails() {

        //Cursor managedCursor = getContentResolver().query("content://sms", null,null, null, null);


        return;
    }

    public void gatherLogs(View view) {
        //Log.d("gatherLogs", "first");
        getCallDetails();
        //Log.d("gatherLogs", "second");
        String toast = Integer.toString(callData.getAmountCalls()) + " " + Integer.toString(callData.getAverageDuration());
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();

    }



    public void exportDb(View view) {
        try {
            backupDatabase();
            Log.d("backup!", "success!");
            Toast.makeText(context, "exported database", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backupDatabaseMail() throws IOException {
        String columnString =   "\"PersonName\",\"Gender\",\"Street1\",\"postOffice\",\"Age\"";
        String dataString   =   "\"" + "username" +"\",\"" + "usergender" + "\",\"" + "useradress" + "\",\"" + "userpostoffice" + "\",\"" + "userage" + "\"";
        String combinedString = columnString + "\n" + dataString;

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/PersonData");
            dir.mkdirs();
            file   =   new File(dir, "Data.csv");
            FileOutputStream out   =   null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(combinedString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri u1 = null;
        u1  =   Uri.fromFile(file);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        sendIntent.setType("text/html");
        startActivity(sendIntent);
    }

    public void backupDatabase() throws IOException {
        //File dbFile = getDatabasePath(dbHelper.getDatabaseName());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "csvname.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file), '\t');
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_NOTIFICATIONENTRIES,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            //Log.d("how far did we get?", "this far!");
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort

                String arrStr[] ={curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4) };
                csvWrite.writeNext(arrStr);
            }
            //Log.d("how far did we get?", "no, even further");
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

}
