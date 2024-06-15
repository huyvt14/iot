package com.example.demo;

import com.example.demo.model.CamBien;
import com.example.demo.model.ClientMqtt;
import com.example.demo.model.Data;
import com.example.demo.model.ThietBi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.CamBienService;
import com.example.demo.service.ThietBiService;
import com.example.demo.service.TrangThaiThietBiService;

import jakarta.annotation.PostConstruct;
import com.example.demo.model.TrangThaiThietBi;
@RestController
public class Controller {
	public final ThietBiService thietBiService;
	public final CamBienService camBienService;
	public final TrangThaiThietBiService trangThaiThietBiService;
	public final ClientMqtt clientMqtt;
	public ArrayList<ThietBi> lthietBi = new ArrayList<ThietBi>();
	public ArrayList<CamBien> lcamBien = new ArrayList<CamBien>();
	
	public Controller (ThietBiService thietBiService, CamBienService camBienService, TrangThaiThietBiService trangThaiThietBiService,ClientMqtt clientMqtt) {
		this.camBienService =camBienService;
		this.thietBiService = thietBiService;
		this.trangThaiThietBiService = trangThaiThietBiService;
		this.clientMqtt = clientMqtt;
	}
//	lấy 4 dữ liệu để hiển thị biểu đồ
	@GetMapping("/tt")
	public String TrangThai() {
		Data da = new Data();
		da.setData(camBienService.get4TTCamBienMoiNhat());
		TrangThaiThietBi tttb= trangThaiThietBiService.getTTMoiNhat();
		da.setDen(tttb.getDen());
		da.setQuat(tttb.getQuat());
		return da.toString();		
	}
//	publish MQTT
	private void publishMQTT(byte[] c) {
		try {
			clientMqtt.publish(c, "test_sub");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
//	nhận tín hiệu bật tắt đèn, quạt từ REACT và gửi tín hiệu cho MQTT Broker
	@MessageMapping("/websocket/battat")
    public void receiveMessage(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Received message ws: " + message);
        byte[] c= message.getBytes();
        publishMQTT(c); 
        System.out.println("da gui");
    }
// chạy khởi động tín hiệu đèn, quạt cho phần cứng
//	@PostConstruct
//	public void khoiDong() {
//		TrangThaiThietBi tt = trangThaiThietBiService.getTTMoiNhat();
//		String s= String.valueOf(tt.getDen())+String.valueOf(tt.getQuat()); 
//		this.publishMQTT(s.getBytes());
//	}	
//	lấy lịch sử của cảm biến, đèn
	@GetMapping("/lichsudulieu")
	public String LichSuDuLieu() {
		lcamBien = camBienService.findAll();
		return trangCamBienDau();
	}
//	xử lý trang đầu cảm biến
	public String trangCamBienDau() {
		int i=10;
		if(lcamBien.size()<10) {
			i= lcamBien.size();
		}
		return lcamBien.subList(0, i).toString();
	}
//	xử lý trang tiếp theo của cảm biến
	@GetMapping("lichsudulieu/trangtieptheo")
	private String trangttcb(@RequestParam int trang) {
		int vt=(trang-1)*10;
		int vtc=vt+10;
		if(vt>=lcamBien.size()) {
			vt=lcamBien.size()-1;
			vtc=vt;
		}
		else {
			if(lcamBien.size()< vtc) {
				vtc= lcamBien.size();
			}
		}
		return lcamBien.subList(vt, vtc).toString();
	}
//	sắp xếp dữ liệu cảm biến/
	@GetMapping("/lichsudulieu/sapxep")
	public String sapXep(@RequestParam  String tieuchi, @RequestParam  String kieu) {
		switch (tieuchi) {
			case "as": {
				if(kieu.equals("tangdan")) {
					Collections.sort(lcamBien, new Comparator<CamBien>() {
						@Override
						public int compare(CamBien cb1, CamBien cb2) {
							return (int) (cb1.getAs()-cb2.getAs());
						}
					});
				}
				else {
					if(kieu.equals("giamdan")) {
						Collections.sort(lcamBien, new Comparator<CamBien>() {
							@Override
							public int compare(CamBien cb1, CamBien cb2) {
								return (int) (cb2.getAs()-cb1.getAs());
							}
						});
					}
				}
				break;
			}
			case "nd": {
				if(kieu.equals("tangdan")) {
					Collections.sort(lcamBien, new Comparator<CamBien>() {
						@Override
						public int compare(CamBien cb1, CamBien cb2) {
							return (int) (cb1.getNd()-cb2.getNd());
						}
					});
				}
				else {
					if(kieu.equals("giamdan")) {
						Collections.sort(lcamBien, new Comparator<CamBien>() {
							@Override
							public int compare(CamBien cb1, CamBien cb2) {
								return (int) (cb2.getNd()-cb1.getNd());
							}
						});
					}
				}
				break;
			}
			default:
				if(kieu.equals("tangdan")) {
					Collections.sort(lcamBien, new Comparator<CamBien>() {
						@Override
						public int compare(CamBien cb1, CamBien cb2) {
							return (int) (cb1.getDa()-cb2.getDa());
						}
					});
				}
				else {
					if(kieu.equals("giamdan")) {
						Collections.sort(lcamBien, new Comparator<CamBien>() {
							@Override
							public int compare(CamBien cb1, CamBien cb2) {
								return (int) (cb2.getDa()-cb1.getDa());
							}
						});
					}
				}
				break;
			}
		return trangCamBienDau();
	}
//	lọc lịch sử cảm biến.
	@GetMapping("/lichsudulieu/timkiem")
	public String locCamBien(@RequestParam  String gio,@RequestParam  String phut,@RequestParam  String ngaybd,
			@RequestParam  String ngaykt, @RequestParam  String nd, @RequestParam  String as, @RequestParam  String da) {
		LichSuDuLieu();
		if(ngaykt.length()>8) {
			ngaykt=ngaykt.substring(0,10)+" "+ngaykt.substring(10);
			System.out.println(ngaykt);
		}
		if(ngaybd.length()>8 ) {
			ngaybd=ngaybd.substring(0,10)+" "+ngaybd.substring(10);
			System.out.println(ngaybd);
		}
		if(ngaybd.length()<9 && ngaykt.length()<9 && gio.length()<1 && phut.length()<1) {	
		}
		else {
			if(gio.length()==1) {
				gio="0"+gio;
			}
			if(gio.length()==1) {
				gio="0"+gio;
			}
			String giobd="00";
			String giokt="23";
			String phutbd="00";
			String phutkt="59";
			if(gio.length()==2){
		        giobd=gio;
		        giokt=gio;
		      }
		    if(phut.length()==2){
		        phutbd=phut;
		        phutkt=phut;
		    }
		    if(ngaybd.length()>10&&ngaykt.length()>10){
		    	ArrayList<CamBien> li= new ArrayList<CamBien>();
				for(CamBien t : this.lcamBien) {
					if(t.getName().toString().compareTo(ngaybd)>=0 && t.getName().toString().compareTo(ngaykt)<=0
					  && t.getName().toString().substring(11,13).compareTo(giobd) >=0 && t.getName().toString().substring(11,13).compareTo(giokt) <=0 
			          &&t.getName().toString().substring(14,16).compareTo(phutbd) >=0 && t.getName().toString().substring(14,16).compareTo(phutkt) <=0 ) {
						li.add(t);
					}
				}
				lcamBien=li;
		    }
		    else {
		    	if(ngaybd.length()<10&&ngaykt.length()<10) {
		    		ArrayList<CamBien> li= new ArrayList<CamBien>();
					for(CamBien t : this.lcamBien) {
						if(
						  t.getName().toString().substring(11,13).compareTo(giobd) >=0 && t.getName().toString().substring(11,13).compareTo(giokt) <=0 
				          &&t.getName().toString().substring(14,16).compareTo(phutbd) >=0 && t.getName().toString().substring(14,16).compareTo(phutkt) <=0 ) {
							li.add(t);
						}
					}
					lcamBien=li;
		    	}
		    	else {
		    		String ngay="";
		    		if(ngaybd.length()<10) {
		    			ngay=ngaykt.substring(0,10);
		    			
		    		}
		    		else {
		    			ngay=ngaybd.substring(0,10);
		    			
		    		}
		    		ArrayList<CamBien> li= new ArrayList<CamBien>();
					for(CamBien t : this.lcamBien) {
						if(t.getName().toString().substring(0,10).compareTo(ngay)==0
						  && t.getName().toString().substring(11,13).compareTo(giobd) >=0 && t.getName().toString().substring(11,13).compareTo(giokt) <=0 
				          &&t.getName().toString().substring(14,17).compareTo(phutbd) >=0 && t.getName().toString().substring(14,17).compareTo(phutkt) <=0 ) {
							li.add(t);
						}
					}
					lcamBien=li;
		    	}
		    }
		}
		float asd=0;
		float asc=999;
		float ndd=0;
		float ndc=999;
		float dad=0;
		float dac=999;
		if(as.length()>1){
		      String s[] = as.split("-");
		      if(s.length==2){
		        asd=Float.parseFloat(s[0]);
		        asc=Float.parseFloat(s[1]);;
		      }else{
		        asd=Float.parseFloat(s[0]);
		        asc=Float.parseFloat(s[0]);
		      }
		    }
	    if(nd.length()>1){
	      String s[]=nd.split("-");
	      if(s.length==2){
	        ndd=Float.parseFloat(s[0]);
	        ndc=Float.parseFloat(s[1]);;
	      }else{
	        ndd=Float.parseFloat(s[0]);
	        ndc=Float.parseFloat(s[0]);
	      }
	    }
	    if(da.length()>1){
	      String s[]=da.split("-");
	      if(s.length==2){
	        dad=Float.parseFloat(s[0]);
	        dac=Float.parseFloat(s[1]);;
	      }else{
	        dad=Float.parseFloat(s[0]);
	        dac=Float.parseFloat(s[0]);
	      }
	    }
	    if(!(as.length()<1 && nd.length()<1 && da.length()<1)){
	    	ArrayList<CamBien> li= new ArrayList<CamBien>();
			for(CamBien t : this.lcamBien) {
				if(t.getAs()>= asd && t.getAs() <= asc && t.getDa()>= dad && t.getDa() <= dac && t.getNd()>= ndd && t.getNd() <= ndc ) {
					li.add(t);
				}
			}
			lcamBien=li;
	    }
		return trangCamBienDau();
	}
//	lấy lịch sử bật tắt bóng đèn
	@GetMapping("/lichsubattat")
	public String LichSuBatTat() {
		lthietBi = thietBiService.findAll();
		return this.trangThietBiDau();
	}
//	xử lý trang trang đầu bật tắt bóng đèn.
	@GetMapping("lichsubattat/trang")
	public String trangThietBiDau() {
		int i=10;
		if(lthietBi.size()<10) {
			i= lthietBi.size();
		}
		return lthietBi.subList(0, i).toString();
	}
//	lọc lịch sử bật tắt bóng đèn
	@GetMapping("lichsubattat/loc")
	private String locThietBi(@RequestParam  String tb, @RequestParam  String tt, @RequestParam  String ngaybd, @RequestParam  String ngaykt ) {
		lthietBi = thietBiService.findAll();
		if(ngaybd.length()>8 && ngaykt.length()>8) {
			ngaybd=ngaybd.substring(0,10)+" "+ngaybd.substring(10);
			ngaykt=ngaykt.substring(0,10)+" "+ngaykt.substring(10);
			ArrayList<ThietBi> li= new ArrayList<ThietBi>();
			for(ThietBi t : this.lthietBi) {
				if(t.getThoiDiem().toString().compareTo(ngaybd)>=0 && t.getThoiDiem().toString().compareTo(ngaykt)<=0) {
					li.add(t);
				}
			}
			lthietBi=li;
		}
		if(!tb.equals("2")) {
			ArrayList<ThietBi> l= new ArrayList<ThietBi>();
			for(ThietBi t : this.lthietBi) {
				if(t.getTen().equals(tb)) {
					l.add(t);
				}
			}
			lthietBi=l;	
		}
		if(!tt.equals("2")) {
			ArrayList<ThietBi> l= new ArrayList<ThietBi>();
			for(ThietBi t : this.lthietBi) {
				if(t.getDieuKhien().equals(tt)) {
					l.add(t);
				}
			}
			lthietBi=l;	
		}
		return trangThietBiDau();  
	}
//	xử lý trang tiếp theo của bật tắt bóng đèn
	@GetMapping("lichsubattat/trangtieptheo")
	private String trangttbt(@RequestParam int trang) {
		int vt=(trang-1)*10;
		int vtc=vt+10;
		if(vt>=lthietBi.size()) {
			vt=lthietBi.size()-1;
			vtc=vt;
		}
		else {
			if(lthietBi.size()< vtc) {
				vtc= lthietBi.size();
			}
		}
		return lthietBi.subList(vt, vtc).toString();
	}
}
