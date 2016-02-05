package com.funfit.usjr.thesis.funfitv2.model;

import java.util.HashMap;

/**
 * Created by Dj on 1/19/2016.
 */
public class User {
    private String fname;
    private String lname;
    private String email;
    private String gender;
    private String dob;
    private String img_url;
    private String weight;
    private String height;
    private String activity_level;
    private String cluster;
    private HashMap<String, Object> timestampJoined;

    public User() {
    }

    public User(String fname, String lname, String email, String gender, String dob, String img_url,
                String weight, String height, String activity_level, String cluster, HashMap<String, Object> timestampJoined) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.img_url = img_url;
        this.weight = weight;
        this.height = height;
        this.activity_level = activity_level;
        this.cluster = cluster;
        this.timestampJoined = timestampJoined;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getActivity_level() {
        return activity_level;
    }

    public String getCluster() {
        return cluster;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }
}