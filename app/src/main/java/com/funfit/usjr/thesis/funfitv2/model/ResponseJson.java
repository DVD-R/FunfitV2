package com.funfit.usjr.thesis.funfitv2.model;

import java.io.Serializable;

public class ResponseJson{

	private int responseId;
	private double rdi;
	private String encodePolyline;
	

	public int getResponseId() {
		return responseId;
	}

	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}

	public double getRdi() {
		return rdi;
	}

	public void setRdi(double rdi) {
		this.rdi = rdi;
	}

	public String getEncodePolyline() {
		return encodePolyline;
	}

	public void setEncodePolyline(String encodePolyline) {
		this.encodePolyline = encodePolyline;
	}		
}
