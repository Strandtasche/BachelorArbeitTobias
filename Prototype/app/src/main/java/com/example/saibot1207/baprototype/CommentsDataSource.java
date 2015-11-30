package com.example.saibot1207.baprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saibot1207 on 07.11.15.
 */
//@SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
public final class CommentsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    //private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NOTIFICATIONENTRY};
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,  MySQLiteHelper.COLUMN_NOTIFICATIONENTRY, MySQLiteHelper.COLUMN_TITLEHASHED, MySQLiteHelper.COLUMN_TEXTLENGTH};


    public CommentsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public NotificationEntry createNotificationEntry(String notificationEntry, String title) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NOTIFICATIONENTRY, notificationEntry);
        values.put(MySQLiteHelper.COLUMN_TITLEHASHED, title);
        long insertId = database.insert(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        NotificationEntry newNotificationEntry = cursorToNotificationEntry(cursor);
        cursor.close();
        return newNotificationEntry;
    }

    public void deleteNotificationEntry(NotificationEntry notificationEntry) {
        long id = notificationEntry.getId();
        System.out.println("NotificationEntry deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<NotificationEntry> getAllNotificationEntries() {
        List<NotificationEntry> notificationEntries = new ArrayList<NotificationEntry>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTIFICATIONENTRIES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NotificationEntry notificationEntry = cursorToNotificationEntry(cursor);
            notificationEntries.add(notificationEntry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return notificationEntries;
    }

    private NotificationEntry cursorToNotificationEntry(Cursor cursor) {
        NotificationEntry notificationEntry = new NotificationEntry();
        notificationEntry.setId(cursor.getLong(0));
        notificationEntry.setNotificationEntry(cursor.getString(1));
        return notificationEntry;
    }
}
