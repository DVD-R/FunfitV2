package com.funfit.usjr.thesis.funfitv2.leaderBoard;

/**
 * Created by ocabafox on 2/24/2016.
 */
public class FilterData {
    private int notificationType;
    private String playerTwoUsername;
    private String name;
    private String imageUrl;
    private String score;

    public FilterData(String name, String imageUrl, String score, int notificationType) {
        this.notificationType = notificationType;
        this.name = name;
        this.imageUrl = imageUrl;
        this.score = score;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public int getNotificationType(){
        return notificationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


    public String getPlayerTwoUsername() {
        return playerTwoUsername;
    }
}
