package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.demo.model.TrangThaiThietBi;
import com.example.demo.repository.TrangThaiThietBiRepository;

@Service
public class TrangThaiThietBiService {
	private final TrangThaiThietBiRepository trangThaiThietBiRepository;
	public TrangThaiThietBiService(TrangThaiThietBiRepository trangThaiThietBiRepository) {
		this.trangThaiThietBiRepository= trangThaiThietBiRepository;
	}
	public ArrayList<TrangThaiThietBi> findAll(){
		return trangThaiThietBiRepository.findAll();
	}
	public void insert(TrangThaiThietBi trangThaiThietBi) {
		trangThaiThietBiRepository.save(trangThaiThietBi);
	}
	public TrangThaiThietBi getTTMoiNhat() {
		return trangThaiThietBiRepository.findFirstByOrderByThoiDiemDesc();
	}

}
