package com.example.macowner.flickr_api.model;

import java.util.ArrayList;

/**
 * Created by Mikhail on 9/29/16.
 */
public class Photos {

    private ArrayList<Photo> photo = new ArrayList<Photo>();


    private Photos(ArrayList<Photo> photo) {
        this.photo = photo;
    }

    public ArrayList<Photo> getPhoto() {
        return photo;
    }
}
