package com.funfit.usjr.thesis.funfitv2.model;

import java.io.Serializable;

public class RequestRun implements Serializable{
	
	private long runId;
	private String date;
	private long distance;
	private long time;
	private long userId;
	
	public RequestRun(){}

	public RequestRun(long runId, String date, long distance, long time, long userId) {
		super();
		this.runId = runId;
		this.date = date;
		this.distance = distance;
		this.time = time;
		this.userId = userId;
	}

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
