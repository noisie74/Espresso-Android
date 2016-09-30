package com.example.macowner.flickr_api.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

  public PollService(){
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.i(TAG, "Received an intent: " + intent);
  }
}
