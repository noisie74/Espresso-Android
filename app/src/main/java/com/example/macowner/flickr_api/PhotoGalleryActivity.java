package com.example.macowner.flickr_api;

import android.support.v4.app.Fragment;


public class PhotoGalleryActivity extends SingleFragmentActivity {

  @Override
  public Fragment createFragment() {
    return PhotoGalleryFragment.newInstance();
  }

}
