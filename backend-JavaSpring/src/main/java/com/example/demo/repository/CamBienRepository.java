package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CamBien;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface CamBienRepository extends JpaRepository<CamBien, Timestamp>{
	 ArrayList<CamBien> findAll();
	 ArrayList<CamBien> findTop4ByOrderByNameDesc();
}
