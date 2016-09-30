package com.example.macowner.flickr_api.model;

/**
 * Created by macowner on 9/29/16.
 */
public class GalleryItem {

  private String mCaption;
  private String mId;
  private String url_s;

  @Override
  public String toString() {
    return mCaption;
  }

  public String getCaption() {
    return mCaption;
  }

  public void setCaption(String caption) {
    mCaption = caption;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

  public String getUrl() {
    return url_s;
  }

  public void setUrl(String url) {
    url_s = url;
  }
}
