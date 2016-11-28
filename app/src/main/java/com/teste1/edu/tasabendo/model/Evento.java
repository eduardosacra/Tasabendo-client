package com.teste1.edu.tasabendo.model;

import java.util.Calendar;

/**
 * Created by edu on 11/11/16.
 */

public class Evento {

    String id;
    String catecategory;
    Calendar create_at;
    String latitude;
    String longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatecategory() {
        return catecategory;
    }

    public void setCatecategory(String catecategory) {
        this.catecategory = catecategory;
    }

    public Calendar getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Calendar create_at) {
        this.create_at = create_at;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
