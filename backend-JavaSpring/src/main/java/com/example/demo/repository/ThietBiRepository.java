package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ThietBi;

import java.util.ArrayList;

public interface ThietBiRepository extends JpaRepository<ThietBi, Integer>{
	 ArrayList<ThietBi> findAll();
	 ThietBi findFirstByOrderByIdDesc();
}
