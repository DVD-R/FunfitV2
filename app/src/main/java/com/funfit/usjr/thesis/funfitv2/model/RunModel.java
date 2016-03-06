package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by ocabafox on 3/6/2016.
 */
public final class RunModel {
    public final long calories;
    public final long time;
    public final long distance;

    public RunModel(long calories, long time, long distance){
        this.calories = calories;
        this.time = time;
        this.distance = distance;
    }
}
