package com.example.macowner.flickr_api.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.macowner.flickr_api.R;
import com.example.macowner.flickr_api.model.GalleryItem;

import java.util.List;

/**
 * Created by macowner on 9/29/16.
 */


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

  private List<GalleryItem> mGalleryItems;
  Context context;


  public PhotoAdapter(List<GalleryItem> galleryItems) {
    mGalleryItems = galleryItems;
  }

  public static class PhotoHolder extends RecyclerView.ViewHolder {
    private ImageView mItemImageView;

    public PhotoHolder(View itemView) {
      super(itemView);

      mItemImageView = (ImageView) itemView.findViewById(R.id.gallery_item_image_view);
    }


  }

  @Override
  public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    context = viewGroup.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
    return new PhotoHolder(view);

  }

  @Override
  public void onBindViewHolder(PhotoHolder holder, int position) {
    GalleryItem galleryItem = mGalleryItems.get(position);

    Glide.with(context).load(galleryItem.getUrl())
        .placeholder(R.drawable.placeholder)
        .into(holder.mItemImageView);
  }

  @Override
  public int getItemCount() {
    return mGalleryItems.size();
  }
}