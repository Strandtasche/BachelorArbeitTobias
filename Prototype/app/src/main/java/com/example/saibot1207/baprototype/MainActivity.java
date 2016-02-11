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

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

//import com.example.saibot1207.baprototype.logger.Log;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Permission;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

        //Check Permissions:
//        if (UStats.getUsageStatsList(this).isEmpty()){
//            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//            startActivity(intent);
//        }
        //Log.d("onCreate", "after usageStat access");



        intentService = new Intent(this, NotificationService.class);
        startService(intentService);

        context = getApplicationContext();

        //tab = (TableLayout) findViewById(R.id.tab);
        //LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        dbHelper = new MySQLiteHelper(this);

        callData = new CallData();

        Log.d("onCreate", "starting up!");


    }





//    private BroadcastReceiver onNotice = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String pack = intent.getStringExtra("package");
//            String title = intent.getStringExtra("title");
//            String text = intent.getStringExtra("text");
//
//
//            TableRow tr = new TableRow(getApplicationContext());
//            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//            TextView textview = new TextView(getApplicationContext());
//            textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
//            textview.setTextSize(20);
//            textview.setTextColor(Color.parseColor("#0B0719"));
//            textview.setText(Html.fromHtml(pack + "<br><b>" + title + " : </b>" + text));
//            tr.addView(textview);
//            tab.addView(tr);
//
//
//        }
//    };

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


    public void checkPermissionsNotificationListener(View v) {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }

    public void checkPermissionsUsageStat(View v) {
        Log.d("checkPermissions", "clicked");
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);

    }

    public void checkPermissionsSuper(View v) {
        Log.d("checkPermissions", "clicked");
        int permissionCheck0 = ContextCompat.checkSelfPermission(context,
                Manifest.permission.PACKAGE_USAGE_STATS);
        int permissionCheck1 = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CALL_LOG);
        int permissionCheck2 = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_SMS);
        int permissionCheck3 = ContextCompat.checkSelfPermission(context,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE);

        String toast = Integer.toString(permissionCheck0) + "\n" + Integer.toString(permissionCheck1) + "\n" + Integer.toString(permissionCheck2) + "\n" + Integer.toString(permissionCheck3);

        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();

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
        Log.d("addStuff", "pressed");
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
        Date resetDate = new Date(1451602800000l); // Alle Anrufe Nach 1.1.2016!
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[] { CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE }, CallLog.Calls.DATE + ">?", new String[] { String.valueOf(resetDate.getTime())}, null);
        //getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        if (managedCursor.getCount() < 1) {
            managedCursor.close();
            return;
        }

        int amount = 0;
        int amountOutgoing = 0;
        int amountIncoming = 0;
        int amountMissed = 0;



        //int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        //int date = managedCursor.getColumnIndex(CallLog.Calls.DATE); // Milliseconds since epoch.
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        int totalDuration = 0;
        int incomingDuration = 0;
        int outgoingDuration = 0;

        //for (int i = 0; i < 3; i++) {
        //    managedCursor.moveToNext();


//        if (!managedCursor.moveToFirst()) {
//            return;
//        }



        while (managedCursor.moveToNext()) { // && ungetestet! TODO!
            amount++;
            //String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            //String callDate = managedCursor.getString(date);
            //Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
//            Log.d("getCallDetails", "duration: " + callDuration);
//            Log.d("getCallDetails", "callDate: " + callDate);
//            Log.d("getCallDetails", "Sysdate: " + Long.toString(System.currentTimeMillis() - Long.valueOf(callDate)));
//
//            long yourmilliseconds = System.currentTimeMillis();
//            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
//            Date resultdate = new Date(yourmilliseconds);
//            Date resultdate2 = new Date(Long.valueOf(callDate));
//            System.out.println(sdf.format(resultdate));
//            System.out.println(sdf.format(resultdate2));

            int durationPure = Integer.parseInt(callDuration);
            totalDuration += durationPure;
            //String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    //dir = "OUTGOING";
                    amountOutgoing++;
                    outgoingDuration += durationPure;
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    //dir = "INCOMING";
                    amountIncoming++;
                    incomingDuration += durationPure;
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    //dir = "MISSED";
                    amountMissed++;
                    break;
            }
        }
        managedCursor.close();
        callData.setAmountIncoming(amountIncoming);
        callData.setAmountMissed(amountMissed);
        callData.setAmountOutgoing(amountOutgoing);
        callData.setTotalDuration(totalDuration);
        callData.setIncomingDuration(incomingDuration);
        callData.setOutgoingDuration(outgoingDuration);
        return;

    }

    private void getMessageDetails() {

        Uri messages = Uri.parse("content://sms");
        Date resetDate = new Date(1451602800000l);

        //Log.d("getMessageDetails", "passed");
        Cursor managedCursor = getContentResolver().query(messages, new String[] { Telephony.TextBasedSmsColumns.DATE, Telephony.TextBasedSmsColumns.BODY, Telephony.TextBasedSmsColumns.TYPE }, Telephony.TextBasedSmsColumns.DATE + ">?", new String[] { String.valueOf(resetDate.getTime())}, null);
        //Cursor managedCursor = getContentResolver().query(messages, null, null, null, null);

        Log.d("getMessageDetails", Integer.toString(managedCursor.getCount()));

        if (managedCursor.getCount() < 1) {
            //Log.d("getMessageDetails", "entered if condition!");
            managedCursor.close();
            //Log.d("getMessageDetails", "lastWayout");
            return;
        }
        //Log.d("getMessageDetails", "passed2");

        int amount = 0;
        int amountSent = 0;
        int amountReceived = 0;

        int totalLength = 0;
        int lengthSent = 0;
        int lengthReceived = 0;

        int type = managedCursor.getColumnIndex(Telephony.TextBasedSmsColumns.TYPE);
        int date = managedCursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE); // Milliseconds since epoch.
        int body = managedCursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);


        //for (int i = 0; i < 10; i++) {
        //    managedCursor.moveToNext();

//        if (!managedCursor.moveToFirst()) {
//            return;
//        }

        Log.d("getMessageDetails", "moving next");
        while (managedCursor.moveToNext()) {

            String messageType = managedCursor.getString(type);
            String messageDate = managedCursor.getString(date);
            String messageBody = managedCursor.getString(body);

            int bodyLength = messageBody.length();

            totalLength += bodyLength;

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date messageDayTime = new Date(Long.valueOf(messageDate));
            System.out.println(sdf.format(messageDayTime));

            //Log.d("getSmsDetails", "body: " + messageBody);
            Log.d("getSmsDetails", "bodyLength: " + bodyLength);
            //Log.d("getSmsDetails", "messageDate: " + messageDate);
            Log.d("getSmsDetails", "type: " + messageType);

            //String dir = null;
            int dircode = Integer.parseInt(messageType);
            switch (dircode) {
                case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT:
                    //dir = "OUTGOING";
                    amountSent++;
                    amount++;
                    lengthSent += bodyLength;
                    break;

                case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX:
                    //dir = "INCOMING";
                    amountReceived++;
                    amount++;
                    lengthReceived += bodyLength;
                    break;

                default:
                    amount++;
                    break;
            }

        }
        managedCursor.close();
        //Log.d("getSmsDetails", Integer.toString(Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX));
        //Log.d("getSmsDetails", Integer.toString(Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT));
        callData.setMessagesAmount(amount);
        callData.setMessagesSend(amountSent);
        callData.setMessagesReceived(amountReceived);
        callData.setTotalMessageLength(totalLength);
        callData.setSentMessageLength(lengthSent);
        callData.setReceivedMessageLength(lengthReceived);
        return;
    }

    public void gatherLogs(View view) {
        //System.out.println("gatherLogs + first");
        getCallDetails();
        getMessageDetails();
        //System.out.println("gatherLogs + second");
        String toast = Integer.toString(callData.getMessagesAmount()) + " " + Integer.toString(callData.getAmountCalls());
        //System.out.println("gatherLogs + third");
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        //Log.d("test123", "test456");
        //System.out.println("gatherLogs + fourth");

    }

    public void usageStatTest(View v) {
        UStats.printCurrentUsageStatus(MainActivity.this);
        Resources res = getResources();
        String[] packages = res.getStringArray(R.array.package_array);
        long[] stats = UStats.returnCurrentUsageStatus(MainActivity.this);
        for (int i = 0; i < packages.length; i++) {
            Log.d(packages[i], Long.toString(stats[i]));
        }
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

    public void info(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Look at this dialog!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

//    public void backupDatabaseMail() throws IOException {
//        String columnString =   "\"PersonName\",\"Gender\",\"Street1\",\"postOffice\",\"Age\"";
//        String dataString   =   "\"" + "username" +"\",\"" + "usergender" + "\",\"" + "useradress" + "\",\"" + "userpostoffice" + "\",\"" + "userage" + "\"";
//        String combinedString = columnString + "\n" + dataString;
//
//        File file   = null;
//        File root   = Environment.getExternalStorageDirectory();
//        if (root.canWrite()){
//            File dir    =   new File (root.getAbsolutePath() + "/PersonData");
//            dir.mkdirs();
//            file   =   new File(dir, "Data.csv");
//            FileOutputStream out   =   null;
//            try {
//                out = new FileOutputStream(file);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            try {
//                out.write(combinedString.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        Uri u1 = null;
//        u1  =   Uri.fromFile(file);
//
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
//        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
//        sendIntent.setType("text/html");
//        startActivity(sendIntent);
//    }

    public void backupDatabase() throws IOException {
        //File dbFile = getDatabasePath(dbHelper.getDatabaseName());
        File exportDir = new File(Environment.getExternalStorageDirectory() + File.separator + "BA_Tobi", "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file1 = new File(exportDir, "csvname1.csv");
        File file2 = new File(exportDir, "csvname2.csv");
        File file3 = new File(exportDir, "csvname3.csv");
        try
        {
            file1.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file1), '\t');
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_NOTIFICATIONENTRIES,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            Log.d("how far did we get?", "this far!");
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort

                String arrStr[] ={curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4)};
//                for (String str : arrStr) {
//                    str = str + ";";
//                }
                csvWrite.writeNext(arrStr);
            }


            //Log.d("how far did we get?", "no, even further");
            csvWrite.close();
            curCSV.close();


            csvWrite = new CSVWriter(new FileWriter(file2), '\t');
            String columns[] = {"AmountCalls", "AmountIncoming", "AmountOutgoing", "AmountMissed", "TotalDuration", "AverageDuration", "IncomingDuration", "OutgoingDuration",
                "AverageIncomingDuration", "AverageOutgoingDuration", "MessagesAmount", "MessagesSent", "MessagesReceived", "TotalMessageLength", "SentMessageLength",
                "ReceivedMessageLength", "AverageMessageLength", "AverageSentMessageLength", "AverageReceivedMessageLength"};
            getMessageDetails();
            getCallDetails();
            String callDataString[] = callData.getStringData().split("#");
            csvWrite.writeNext(columns);
            csvWrite.writeNext(callDataString);
            csvWrite.close();

            csvWrite = new CSVWriter(new FileWriter(file3), '\t');
            Resources res = getResources();
            String[] packages = res.getStringArray(R.array.package_array);
            long[] stats = UStats.returnCurrentUsageStatus(MainActivity.this);
            String[] stringArray = new String[stats.length];
            for(int i = 0; i < stats.length; i++){
                stringArray[i] = String.valueOf(stats[i]);
            }
            csvWrite.writeNext(packages);
            csvWrite.writeNext(stringArray);
            csvWrite.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

}
