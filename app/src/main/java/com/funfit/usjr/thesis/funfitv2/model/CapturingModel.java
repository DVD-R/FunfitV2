package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by ocabafox on 2/9/2016.
 */
public final class CapturingModel {
    private String faction_description;
    private int userId;
    private int territoryId;


    public CapturingModel(){

    }


    public CapturingModel(String faction_description, int userId, int territoryId){
        this.faction_description = faction_description;
        this.userId = userId;
        this.territoryId = territoryId;
    }


    public String getFaction_description() {
        return faction_description;
    }

    public void setFaction_description(String faction_description) {
        this.faction_description = faction_description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(int territoryId) {
        this.territoryId = territoryId;
    }
}
