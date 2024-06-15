package com.example.demo.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cambien")
public class CamBien {
	@Id
	@Column(name = "thoidiem")
	private Timestamp name;
	@Column(name = "anhsang")
	private float as;
	@Column(name = "doam")
	private float da;
	@Column(name = "nhietdo")
	private float nd;
	@Column (name = "dobui")
	private float db;
	
	public float getDb() {
		return db;
	}
	public void setDb(float db) {
		this.db = db;
	}
	public CamBien() {
	}
	public Timestamp getName() {
		return name;
	}
	public void setName(Timestamp name) {
		this.name = name;
	}
	public float getAs() {
		return as;
	}
	public void setAs(float as) {
		this.as = as;
	}
	public float getDa() {
		return da;
	}
	public void setDa(float da) {
		this.da = da;
	}
	public float getNd() {
		return nd;
	}
	public void setNd(float nd) {
		this.nd = nd;
	}
	@Override
	public String toString() {
		ObjectMapper obm = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
