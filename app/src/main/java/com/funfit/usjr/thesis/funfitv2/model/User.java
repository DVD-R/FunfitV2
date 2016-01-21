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
    private HashMap<String, Object> timestampJoined;

    /**
     * Required public constructor
     */
    public User() {
    }

    /**
     * Use this constructor to create new User.
     * Takes user name, email and timestampJoined as params
     *
     * @param fname
     * @param email
     * @param timestampJoined
     */
    public User(String fname, String lname, String email, int gender, String dob,
                String img_url, HashMap<String, Object> timestampJoined) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        if(gender == 0)
            this.gender = "male";
        else
            this.gender = "female";
        this.dob = dob;
        this.img_url = img_url;
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

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }
}