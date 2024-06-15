package com.example.demo.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Data {
	private ArrayList<CamBien> data;
	private int den;
	private int quat;
	public Data() {
		
	}	
	public Data(ArrayList<CamBien> listCB, int den, int quat) {
		this.data = listCB;
		this.den =den;
		this.quat=quat;
	}
	public ArrayList<CamBien> getData() {
		return data;
	}
	public void setData(ArrayList<CamBien> data) {
		this.data = data;
	}
	public int getDen() {
		return den;
	}
	public void setDen(int den) {
		this.den = den;
	}
	public int getQuat() {
		return quat;
	}
	public void setQuat(int quat) {
		this.quat = quat;
	}
	@Override
	public String toString() {
		Collections.reverse(this.data);
		ObjectMapper obm = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
        obm.setDateFormat(df);
		String sjson="";
		try {
			sjson = obm.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return sjson;
	}
}
