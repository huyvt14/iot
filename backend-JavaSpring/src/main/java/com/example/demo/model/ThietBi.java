package com.example.demo.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "thietbi")
public class ThietBi {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "thoidiem")
	private Timestamp thoiDiem;
	private String ten;
	@Column(name = "dieukhien")
	private String dieuKhien;
	
	public ThietBi() {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ThietBi( String ten, String dieuKhien, Timestamp thoiDiem) {
		this.ten=ten;
		this.dieuKhien=dieuKhien;
		this.thoiDiem=thoiDiem;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	public String getDieuKhien() {
		return dieuKhien;
	}
	public void setDieuKhien(String dieuKhien) {
		this.dieuKhien = dieuKhien;
	}
	public Timestamp getThoiDiem() {
		return thoiDiem;
	}
	public void setThoiDiem(Timestamp thoiDiem) {
		this.thoiDiem = thoiDiem;
	}
	@Override
	public String  toString() {
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
