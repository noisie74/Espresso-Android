package com.example.macowner.flickr_api;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.macowner.flickr_api.adapter.PhotoAdapter;
import com.example.macowner.flickr_api.model.GalleryItem;
import com.example.macowner.flickr_api.network.FlickrFetchr;
//import com.example.macowner.flickr_api.service.PollService;
import com.example.macowner.flickr_api.service.PollService;
import com.example.macowner.flickr_api.util.QueryPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail on 9/29/16.
 */
public class PhotoGalleryFragment extends Fragment {

  private RecyclerView mPhotoRecyclerView;
  private List<GalleryItem> mItems = new ArrayList<>();
  private static final String TAG = "PhotoGalleryFragment";


  private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {

    private String mQuery;

    public FetchItemsTask(String query) {
      mQuery = query;
    }

    @Override
    protected List<GalleryItem> doInBackground(Void... params) {
      if (mQuery == null) {
        return new FlickrFetchr().fetchRecentPhotos();
      } else {
        return new FlickrFetchr().searchPhotos(mQuery);
      }
    }

    @Override
    protected void onPostExecute(List<GalleryItem> items) {
      mItems = items;
      setupAdapter();
    }
  }


  public static PhotoGalleryFragment newInstance() {
    return new PhotoGalleryFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    setHasOptionsMenu(true);
    updateItems();

    PollService.setServiceAlarm(getActivity(), true);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

    mPhotoRecyclerView = (RecyclerView) v
        .findViewById(R.id.fragment_photo_gallery_recycler_view);
    mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    setupAdapter();

    return v;
  }

  private void setupAdapter() {
    mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));

  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_photo_gallery, menu);
    MenuItem searchItem = menu.findItem(R.id.menu_item_search);
    final SearchView searchView = (SearchView) searchItem.getActionView();

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String s) {
        Log.d(TAG, "QueryTextSubmit: " + s);
        //set text in shared preferences
        QueryPreferences.setStoredQuery(getActivity(), s);
        updateItems();
        return true;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        Log.d(TAG, "QueryTextChange: " + s);
        return false;
      }
    });

    searchView.setOnSearchClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String query = QueryPreferences.getStoredQuery(getActivity());
        searchView.setQuery(query, false);
      }
    });

//    check the text and state of polling
    MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
    if (PollService.isServiceAlarmOn(getActivity()))
      toggleItem.setTitle(R.string.stop_polling);
    else
      toggleItem.setTitle(R.string.start_polling);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch ((item.getItemId())) {
      case R.id.menu_item_clear:
        QueryPreferences.setStoredQuery(getActivity(), null);
        updateItems();
        return true;
      case R.id.menu_item_toggle_polling:
        boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
        PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
        getActivity().invalidateOptionsMenu();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }

  }

  private void updateItems() {
    String query = QueryPreferences.getStoredQuery(getActivity());
    new FetchItemsTask(query).execute();
  }
}

//https://github.com/daseyffert/PhotoGallery/blob/master/app/src/main/java/com/bignerdranch/android/photogallery/PollService.java