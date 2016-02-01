package com.example.saibot1207.baprototype;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class UStats {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    public static final String TAG = UStats.class.getSimpleName();
    @SuppressWarnings("ResourceType")
    public static void getStats(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        int interval = UsageStatsManager.INTERVAL_YEARLY;
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.set(2016, Calendar.JANUARY, 1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        UsageEvents uEvents = usm.queryEvents(startTime, endTime);
        while (uEvents.hasNextEvent()){
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);

            if (e != null){
                Log.d(TAG, "Event: " + e.getPackageName() + "\t" +  e.getTimeStamp());
            }
        }
    }

    public static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        return usageStatsList;
    }

    public static void printUsageStats(List<UsageStats> usageStatsList){
        for (UsageStats u : usageStatsList){
            Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                    + u.getTotalTimeInForeground()) ;
        }

    }

    public static long[] getUsageStats(List<UsageStats> usageStatsList, Context context){
        HashMap<String, Long> hmap = new HashMap<String, Long>();
        Resources res = context.getResources();
        String[] keys = res.getStringArray(R.array.package_array);
        long[] values = new long[keys.length];
        for (UsageStats u : usageStatsList){
            hmap.put(u.getPackageName(), u.getTotalTimeInForeground());
        }
        int i = 0;
        for (String key : keys){
            if (hmap.containsKey(key)) {
                values[i] = hmap.get(key);
            }
            else {
                values[i] = 0;
            }
            i++;
        }
        return values;
    }

    public static void printCurrentUsageStatus(Context context){
        printUsageStats(getUsageStatsList(context));
    }

    public static long[] returnCurrentUsageStatus(Context context){
        return getUsageStats(getUsageStatsList(context), context);
    }

    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }
}