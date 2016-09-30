package com.example.macowner.flickr_api.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by macowner on 9/30/16.
 */
public class QueryPreferences {

  private static final String PREF_SEARCH_QUERY = "searchQuery";
  private static final String PREF_LAST_RESULT_ID = "lastResultId";


  public static String getStoredQuery(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context)
        .getString(PREF_SEARCH_QUERY, null);
  }

  public static void setStoredQuery(Context context, String query) {
    PreferenceManager.getDefaultSharedPreferences(context)
        .edit()
        .putString(PREF_SEARCH_QUERY, query)
        .apply();
  }

  //returns last result fetched
  public static String getLastResultId(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LAST_RESULT_ID, null);
  }

  //writes the last result fetched
  public static void setLastResultId(Context context, String lastResultId) {
    PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_LAST_RESULT_ID, lastResultId).apply();

  }
}
