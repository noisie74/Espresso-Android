package com.example.macowner.flickr_api;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macowner.flickr_api.adapter.PhotoAdapter;
import com.example.macowner.flickr_api.model.GalleryItem;
import com.example.macowner.flickr_api.network.FlickrFetchr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail on 9/29/16.
 */
public class PhotoGalleryFragment extends Fragment {

  private RecyclerView mPhotoRecyclerView;
  private List<GalleryItem> mItems = new ArrayList<>();
  private static final String TAG = "PhotoGalleryFragment";


  private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
      new FlickrFetchr().fetchItems();
      return null;
    }
  }


  public static PhotoGalleryFragment newInstance() {
    return new PhotoGalleryFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    new FetchItemsTask().execute();
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
    if (!isAdded()) {
      mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
    }
  }

}
