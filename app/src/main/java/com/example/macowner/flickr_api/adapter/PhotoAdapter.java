package com.example.macowner.flickr_api.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.macowner.flickr_api.model.GalleryItem;

import java.util.List;

/**
 * Created by macowner on 9/29/16.
 */


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

  private List<GalleryItem> mGalleryItems;

  public PhotoAdapter(List<GalleryItem> galleryItems) {
    mGalleryItems = galleryItems;
  }

  public static class PhotoHolder extends RecyclerView.ViewHolder {
    private TextView mTitleTextView;

    public PhotoHolder(View itemView) {
      super(itemView);

      mTitleTextView = (TextView) itemView;
    }

    public void bindGalleryItem(GalleryItem item) {
      mTitleTextView.setText(item.toString());
    }
  }

  @Override
  public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    TextView textView = new TextView(viewGroup.getContext());
    return new PhotoHolder(textView);
  }

  @Override
  public void onBindViewHolder(PhotoHolder holder, int position) {
    GalleryItem galleryItem = mGalleryItems.get(position);
    holder.bindGalleryItem(galleryItem);
  }

  @Override
  public int getItemCount() {
    return mGalleryItems.size();
  }
}