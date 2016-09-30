package com.example.macowner.flickr_api.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import com.example.macowner.flickr_api.model.GalleryItem;
import com.example.macowner.flickr_api.network.FlickrFetchr;
import com.example.macowner.flickr_api.util.QueryPreferences;

import java.util.List;

/**
 * Created by macowner on 9/30/16.
 */
public class PollService extends IntentService {

  private static final String TAG = "PollService";

  private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

  //activities call this method to create a Service
  public static Intent newIntent(Context context) {
    return new Intent(context, PollService.class);
  }

  public static void setServiceAlarm(Context context, boolean isOn) {
    //construct pending intent that starts PollService
    Intent intent = PollService.newIntent(context);
    //packages up invocation of Context.startService(Intent)
    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    //Set the alarm of cancel it
    if (isOn)
      alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL, pendingIntent);
    else {
      alarmManager.cancel(pendingIntent);
      pendingIntent.cancel();
    }
  }

  public static boolean isServiceAlarmOn(Context context) {
    //check whether pending Intent exist
    Intent intent = PollService.newIntent(context);
    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

    return pendingIntent != null;
  }

  public PollService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    String query = QueryPreferences.getStoredQuery(this);
    String lastResultId = QueryPreferences.getLastResultId(this);
    List<GalleryItem> items;

    if (query == null) {
      items = new FlickrFetchr().fetchRecentPhotos();
    } else {
      items = new FlickrFetchr().searchPhotos(query);
    }

    if (items.size() == 0) {
      return;
    }

    String resultId = items.get(0).getId();
    if (resultId.equals(lastResultId)) {

      Log.i(TAG, "Got an old result: " + resultId);
    } else {
      Log.i(TAG, "Got a new result: " + resultId);
    }

    QueryPreferences.setLastResultId(this, resultId);
  }


  private boolean isNetworkAvailableAndConnected() {
    ConnectivityManager cm =
        (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

    boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
    boolean isNetworkConnected = isNetworkAvailable &&
        cm.getActiveNetworkInfo().isConnected();
    return isNetworkConnected;

  }
}
