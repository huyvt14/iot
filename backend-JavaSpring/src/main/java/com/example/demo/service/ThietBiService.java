package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.demo.model.ThietBi;
import com.example.demo.repository.ThietBiRepository;

@Service
public class ThietBiService {
	private final ThietBiRepository thietBiRepository;	
	public ThietBiService(ThietBiRepository thietBiRepository) {
		this.thietBiRepository= thietBiRepository;
	}
	public ArrayList<ThietBi> findAll(){
		return thietBiRepository.findAll();
	}
	public void insert(ThietBi thietBi) {
		thietBiRepository.save(thietBi);
	}
	public ThietBi getTTMoiNhat() {
		return thietBiRepository.findFirstByOrderByIdDesc();
	}
}
