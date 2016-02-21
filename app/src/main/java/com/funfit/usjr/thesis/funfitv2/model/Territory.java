package com.funfit.usjr.thesis.funfitv2.model;

import java.io.Serializable;

public class Territory implements Serializable{

	private int id;
	private String encoded_polyline;
	private String status;
	private int level;
	private String faction_description;

	public Territory(){}

	public Territory(int id, String encoded_polyline, String status, int level, String faction_description) {
		super();
		this.id = id;
		this.encoded_polyline = encoded_polyline;
		this.status = status;
		this.level = level;
		this.faction_description = faction_description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEncoded_polyline() {
		return encoded_polyline;
	}

	public void setEncoded_polyline(String encoded_polyline) {
		this.encoded_polyline = encoded_polyline;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getFaction_description() {
		return faction_description;
	}

	public void setFaction_description(String faction_description) {
		this.faction_description = faction_description;
	}
}
