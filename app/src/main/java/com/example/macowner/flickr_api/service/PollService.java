package com.example.macowner.flickr_api.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.macowner.flickr_api.PhotoGalleryActivity;
import com.example.macowner.flickr_api.R;
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
  public static final String ACTION_SHOW_NOTIFICATION =
          "com.bignerdranch.android.photogallery.SHOW_NOTIFICATION";

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

    QueryPreferences.setAlarmOn(context,isOn);
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

    if (!isNetworkAvailableAndConnected())
      return;

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


      Resources resources = getResources();
      Intent i = PhotoGalleryActivity.newIntent(this);
      PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

      Notification notification = new NotificationCompat.Builder(this)
          //configure text
          .setTicker(resources.getString(R.string.new_pictures_title))
          //configure small icon, title and text
          .setSmallIcon(android.R.drawable.ic_menu_report_image)
          .setContentTitle(resources.getString(R.string.new_pictures_title))
          .setContentText(resources.getString(R.string.new_pictures_text))
          //specify what happens when user presses notification
          .setContentIntent(pi)
          .setAutoCancel(true)
          .build();

      //get an instance to post notification
      NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
      notificationManagerCompat.notify(0, notification);
      sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION));

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
