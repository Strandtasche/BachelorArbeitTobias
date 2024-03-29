package com.example.saibot1207.baprototype;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Created by saibot1207 on 03.11.15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NOTIFICATIONENTRIES = "notificationEntries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTIFICATIONENTRY = "notificationEntry";
    public static final String COLUMN_TITLEHASHED = "titelHashed";
    public static final String COLUMN_TEXTLENGTH = "textLength";
    public static final String COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "notificationEntries.db";
    private static final int DATABASE_VERSION = 4;
    private String databasePath = "";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NOTIFICATIONENTRIES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NOTIFICATIONENTRY + " text not null, " + COLUMN_TITLEHASHED + " text not null, " + COLUMN_TEXTLENGTH + " text not null," + COLUMN_DATE + " integer not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        databasePath = context.getDatabasePath(DATABASE_NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONENTRIES);
        Log.d("OnUpdate", DATABASE_CREATE);
        onCreate(db);
    }



}
