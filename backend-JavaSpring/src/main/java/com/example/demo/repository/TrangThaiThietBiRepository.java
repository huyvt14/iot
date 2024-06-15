package com.example.demo.repository;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TrangThaiThietBi;

public interface TrangThaiThietBiRepository extends JpaRepository<TrangThaiThietBi, Timestamp>{
	 ArrayList<TrangThaiThietBi> findAll();
	 TrangThaiThietBi findFirstByOrderByThoiDiemDesc();
}
