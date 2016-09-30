package com.example.macowner.flickr_api;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macowner.flickr_api.adapter.PhotoAdapter;
import com.example.macowner.flickr_api.model.GalleryItem;
import com.example.macowner.flickr_api.model.Photo;
import com.example.macowner.flickr_api.network.ApiProvider;
//import com.example.macowner.flickr_api.network.FlickrFetchr;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mikhail on 9/29/16.
 */
public class PhotoGalleryFragment extends Fragment {

  private RecyclerView mPhotoRecyclerView;
  private List<Photo> mItems = new ArrayList<>();
  private static final String TAG = "PhotoGalleryFragment";


//  private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
//    @Override
//    protected List<GalleryItem> doInBackground(Void... params) {
//      return new FlickrFetchr().fetchItems();
//    }
//
//    @Override
//    protected void onPostExecute(List<GalleryItem> items) {
//      mItems = items;
//      Log.d("PhotoGalleryFragment",items.toString());
//      setupAdapter();
//    }
//  }


  public static PhotoGalleryFragment newInstance() {
    return new PhotoGalleryFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
//    new FetchItemsTask().execute();

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

    mPhotoRecyclerView = (RecyclerView) v
        .findViewById(R.id.fragment_photo_gallery_recycler_view);
    mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//    setupAdapter();

    getData();

    return v;
  }

  private void setupAdapter() {

      mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));

  }

  private void getData(){

    ApiProvider.ApiCall mCall = ApiProvider.create();

    Call<GalleryItem>getPhotos = mCall.getRecent("flickr.photos.getRecent",
            ApiProvider.API_KEY,"json","1",ApiProvider.EXTRA_SMALL_URL);

        getPhotos.enqueue(new Callback<GalleryItem>() {
          @Override
          public void onResponse(Call<GalleryItem> call, Response<GalleryItem> response) {

            mItems = response.body().getPhotos().getPhoto();
            setupAdapter();

          }

          @Override
          public void onFailure(Call<GalleryItem> call, Throwable t) {

            Log.d("PhotoGalleryFragment",t.toString());
          }
        });
  }

}
