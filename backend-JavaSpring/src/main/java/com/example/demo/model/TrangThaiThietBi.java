package com.example.demo.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trangthaithietbi")
public class TrangThaiThietBi {
	@Id
	@Column(name = "thoidiem")
	private Timestamp thoiDiem;
	private int den;
	private int quat;
	
	public TrangThaiThietBi() {
		
	}
	public Timestamp getThoiDiem() {
		return thoiDiem;
	}
	public void setThoiDiem(Timestamp thoiDiem) {
		this.thoiDiem = thoiDiem;
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
}
