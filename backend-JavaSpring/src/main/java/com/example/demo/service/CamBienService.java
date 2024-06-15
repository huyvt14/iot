package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import com.example.demo.model.CamBien;
import com.example.demo.repository.CamBienRepository;

@Service
public class CamBienService {
	private final CamBienRepository camBienRepository;
	
	public CamBienService(CamBienRepository camBienRepository) {
		this.camBienRepository =camBienRepository;
	}
	public ArrayList<CamBien> findAll(){
		return camBienRepository.findAll();
	}
	public void insert(CamBien camBien) {
		camBienRepository.save(camBien);
	}
	public ArrayList<CamBien> get4TTCamBienMoiNhat(){
		return camBienRepository.findTop4ByOrderByNameDesc();
	}
}
