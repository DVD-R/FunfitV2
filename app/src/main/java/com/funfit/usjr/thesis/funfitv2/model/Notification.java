package com.funfit.usjr.thesis.funfitv2.model;

import java.util.HashMap;

/**
 * Created by Dj on 1/20/2016.
 */
public class Notification {
    private int notificationType;
    private String playerTwoUsername;
    private String playerTwoImgUrl;
    private HashMap<String, Object> timeStamp;

    public Notification(){}

    public Notification(int notificationType, String playerTwoUsername, String playerTwoImgUrl, HashMap<String, Object> timestampJoined) {
        this.notificationType = notificationType;
        this.playerTwoUsername = playerTwoUsername;
        this.playerTwoImgUrl = playerTwoImgUrl;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public String getPlayerTwoUsername() {
        return playerTwoUsername;
    }

    public String getPlayerTwoImgUrl() {
        return playerTwoImgUrl;
    }

    public HashMap<String, Object> getTimeStamp() {
        return timeStamp;
    }
}
