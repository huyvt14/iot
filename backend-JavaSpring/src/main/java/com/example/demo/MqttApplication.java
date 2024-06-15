package com.example.demo;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.model.ClientMqtt;
import com.example.demo.service.CamBienService;
import com.example.demo.service.ThietBiService;
import com.example.demo.service.TrangThaiThietBiService;

@SpringBootApplication
public class MqttApplication {

	public static void main(String[] args) throws MqttException, InterruptedException  {
		ConfigurableApplicationContext context = SpringApplication.run(MqttApplication.class, args);
		CamBienService camBienService =context.getBean(CamBienService.class);
		ThietBiService thietBiService = context.getBean(ThietBiService.class);
		TrangThaiThietBiService trangThaiThietBiService = context.getBean(TrangThaiThietBiService.class);
		ClientMqtt clientMqtt = context.getBean(ClientMqtt.class);
		clientMqtt.subscribe("test_pub", clientMqtt.mqttClient(), camBienService, thietBiService, trangThaiThietBiService);
	}
		
	@Bean 
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() { 
            @Override 
            public void addCorsMappings(CorsRegistry register) { 
                register.addMapping("/**").allowCredentials(true).allowedOrigins("http://localhost:3000").allowedMethods("*"); 
            } 
        }; 
    }
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

