package com.example.demo.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.stereotype.Component;

import com.example.demo.service.CamBienService;
import com.example.demo.service.ThietBiService;
import com.example.demo.service.TrangThaiThietBiService;

@Component
public class ClientMqtt {
	public  ClientMqtt() {
	}
	public IMqttClient mqttClient() throws MqttException {
		IMqttClient mqttClient = new MqttClient("tcp://localhost:1883", "test1");	
		mqttClient.connect(mqttConnectOptions());
		return mqttClient;
	}
	public static MqttConnectOptions mqttConnectOptions() {
		return new MqttConnectOptions();
	}
	public void subscribe(final String topic, IMqttClient mqttClient, 
			CamBienService camBienService, ThietBiService thietBiService,
			TrangThaiThietBiService trangThaiThietBiService) throws MqttException, InterruptedException {

		System.out.println("Messages received:");
		mqttClient.subscribeWithResponse(topic, (tpic, msg) -> {
			LocalDateTime current = LocalDateTime.now();
			Timestamp time = Timestamp.valueOf(current);
			TrangThaiThietBi trangThaiThietBi = new TrangThaiThietBi();
			TrangThaiThietBi trangThaiThietBic = trangThaiThietBiService.getTTMoiNhat();
			CamBien camBien = new CamBien();
			
			byte[] b= msg.getPayload();
			String s= new String(b);
			System.out.println(s);
			if(s.equals("ESP_reconnected")) return;
			String xl[]= s.split("\\|");
			camBien.setName(time);
			camBien.setAs(Float.parseFloat(xl[0]));
			camBien.setDa(Float.parseFloat(xl[1]));
			camBien.setNd(Float.parseFloat(xl[2]));
			camBien.setDb(Float.parseFloat(xl[5]));
			try {
			    camBienService.insert(camBien);
			} catch (Exception e) {
			}
			trangThaiThietBi.setThoiDiem(time);
			trangThaiThietBi.setDen(Integer.parseInt(xl[3]));
			trangThaiThietBi.setQuat(Integer.parseInt(xl[4]));
			if(trangThaiThietBic.getDen()!=trangThaiThietBi.getDen()) {
				String tt="";
				if(trangThaiThietBi.getDen() == 1) {
					tt="bat";
				}
				else {
					tt="tat";
				}
				ThietBi thietBi = new ThietBi() ;
				thietBi.setThoiDiem(time);
				thietBi.setTen("den");
				thietBi.setDieuKhien(tt);
				try {
					thietBiService.insert(thietBi);
				} catch (Exception e) {
				}
			}
			if(trangThaiThietBic.getQuat()!=trangThaiThietBi.getQuat()) {
				String tt="";
				if(trangThaiThietBi.getQuat() == 1) {
					tt="bat";
				}
				else {
					tt="tat";
				}	
				ThietBi thietBi = new ThietBi() ;
				thietBi.setThoiDiem(time);
				thietBi.setTen("quat");
				thietBi.setDieuKhien(tt);
				try {thietBiService.insert(thietBi);}catch (Exception e) {
				}
			}
			try {
			    trangThaiThietBiService.insert(trangThaiThietBi);
			} catch (Exception e) {
			}
		});
	}
	public void publish(byte[] b, final String topic) throws MqttPersistenceException, MqttException {
		MqttMessage message = new MqttMessage(b);
		IMqttClient mqttClient = new MqttClient("tcp://localhost:1883", "test2");
		mqttClient.connect(mqttConnectOptions());
		message.setQos(2);
	    mqttClient.publish(topic, message);
	    mqttClient.disconnect();
	    mqttClient.close();
	}
}
