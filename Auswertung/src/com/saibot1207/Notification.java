package com.saibot1207;

/**
 * Created by saibot1207 on 11.03.16.
 */


public class Notification {

    public enum appPackage{
        HANGOUTS, WHATSAPP, TOBIAPP, SIGNAL, TELEGRAM, SKYPE, FACEBOOKAPP, FACEBOOKMESS, CARBON, PLUME, TWITTER, TALON, FALCON, OTHER
    }
    int id;
    appPackage app;
    String title;
    int textlength;
    long timestamp;

    public Notification(int id, String app, String title, int textlength, long timestamp) {
        this.id = id;
        this.title = title;
        this.textlength = textlength;
        this.timestamp = timestamp;

        switch (app) {
            case "com.google.android.talk":
                this.app = appPackage.HANGOUTS;
                break;
            case "com.whatsapp":
                this.app = appPackage.WHATSAPP;
                break;
            case "com.example.saibot1207.baprototype":
                this.app = appPackage.TOBIAPP;
                break;
            case "org.thoughtcrime.securesms":
                this.app = appPackage.SIGNAL;
                break;
            case "org.telegram.messenger":
                this.app = appPackage.TELEGRAM;
                break;
            case "com.skype.raider":
                this.app = appPackage.SKYPE;
                break;
            case "com.facebook.katana":
                this.app = appPackage.FACEBOOKAPP;
                break;
            case "com.facebook.orca":
                this.app = appPackage.FACEBOOKMESS;
                break;
            case "com.dotsandlines.carbon":
                this.app = appPackage.CARBON;
                break;
            case "com.levelup.touiteur":
                this.app = appPackage.PLUME;
                break;
            case "com.twitter.android":
                this.app = appPackage.TWITTER;
                break;
            case "com.klinker.android.twitter_l":
                this.app = appPackage.TALON;
                break;
            case "com.jv.materialfalcon":
                this.app = appPackage.FALCON;
                break;
            default:
                this.app = appPackage.OTHER;
                break;

        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public appPackage getApp() {
        return app;
    }

    public void setApp(appPackage app) {
        this.app = app;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTextlength() {
        return textlength;
    }

    public void setTextlength(int textlength) {
        this.textlength = textlength;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getString() {
        return "" + id + " " + app + " " + title + " " + textlength + " " + timestamp;
    }
}
