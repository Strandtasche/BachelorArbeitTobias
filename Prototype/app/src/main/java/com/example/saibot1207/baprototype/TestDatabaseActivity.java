package com.example.saibot1207.baprototype;

import android.app.ListActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.saibot1207.baprototype.R;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class TestDatabaseActivity extends ListActivity {
    private CommentsDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);

        datasource = new CommentsDataSource(this);
        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<NotificationEntry> values = datasource.getAllNotificationEntries();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<NotificationEntry> adapter = new ArrayAdapter<NotificationEntry>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<NotificationEntry> adapter = (ArrayAdapter<NotificationEntry>) getListAdapter();
        NotificationEntry notificationEntry = null;
        switch (view.getId()) {
            case R.id.add:
                String[] notificationEntries = new String[] { "Cool", "Very nice", "Hate it" };
                int nextInt = new Random().nextInt(3);
                // save the new NotificationEntry to the database
                notificationEntry = datasource.createNotificationEntry(notificationEntries[nextInt]);
                adapter.add(notificationEntry);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    notificationEntry = (NotificationEntry) getListAdapter().getItem(0);
                    datasource.deleteNotificationEntry(notificationEntry);
                    adapter.remove(notificationEntry);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}