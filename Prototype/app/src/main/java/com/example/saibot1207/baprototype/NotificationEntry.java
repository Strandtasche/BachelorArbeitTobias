package com.example.saibot1207.baprototype;

/**
 * Created by saibot1207 on 07.11.15.
 */
public class NotificationEntry {

    private long id;
    private String notificationEntry;
    private int titleHashed;
    private int textLength;

    public int getTitleHashed() {
        return titleHashed;
    }

    public void setTitleHashed(int titleHashed) {
        this.titleHashed = titleHashed;
    }

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNotificationEntry() {
        return notificationEntry;
    }

    public void setNotificationEntry(String notificationEntry) {
        this.notificationEntry = notificationEntry;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return notificationEntry;
    }
}
