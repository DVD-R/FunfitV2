package com.funfit.usjr.thesis.funfitv2.model;

import java.io.Serializable;

public class Rdi implements Serializable{
	private double rdi;
	private int userId;

	public Rdi(){}

	public Rdi(double rdi, int userId) {
		super();
		this.rdi = rdi;
		this.userId = userId;
	}

	public double getRdi() {
		return rdi;
	}

	public void setRdi(double rdi) {
		this.rdi = rdi;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
