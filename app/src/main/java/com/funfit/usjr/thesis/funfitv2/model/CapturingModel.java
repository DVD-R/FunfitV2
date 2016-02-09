package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by ocabafox on 2/9/2016.
 */
public final class CapturingModel {
    public final String user_id;
    public final String cluster_description;
    public final long id;
    public final long faction_id;

    public CapturingModel(String user_id, String cluster_description, long id, long faction_id){
        this.user_id = user_id;
        this.cluster_description = cluster_description;
        this.id = id;
        this.faction_id = faction_id;
    }
}
