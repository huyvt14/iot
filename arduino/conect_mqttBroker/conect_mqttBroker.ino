#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include "DHT.h"
#include <Arduino.h>

const int DHTPin = 4;
#define DHTTYPE DHT11 

DHT dht(DHTPin, DHTTYPE);    

// #define ssid "OPPO A57"
// #define password "123456789"
#define ssid "huyen"
#define password "16092002"

int quangtro = A0;
// int cdb = 13;
int cden = 14;
int cquat = 12;

int den = 0;
int quat = 0;
// mosquitto_pub -h 192.168.1.102 -p 1883 -t test_sub -m "11"
// chỉnh lại định dạng đầu ra, code từ dòng 108
// #define mqtt_server "192.168.180.168" 
#define mqtt_server "192.168.61.168" 
#define mqtt_topic_pub "test_pub"   
#define mqtt_topic_sub "test_sub"

const uint16_t mqtt_port = 1883; 
WiFiClient espClient;
PubSubClient client(espClient);

char msg[50];

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
  if ((char)payload[0] == '1') {
    digitalWrite(cden, HIGH);
    den = 1;
  } else {
    digitalWrite(cden, LOW);
    den = 0;
  }
  if ((char)payload[1] == '1') {
    digitalWrite(cquat, HIGH);
    quat = 1;
  } else {
    digitalWrite(cquat, LOW);
    quat = 0;
  }
}

void setup() {
  Serial.begin(9600);
  setup_wifi();
  client.setServer(mqtt_server, mqtt_port); 
  client.setCallback(callback);

  delay(10);
  dht.begin();

  pinMode(cden, OUTPUT);
  pinMode(cquat, OUTPUT);
  // pinMode(cdb, OUTPUT);
  digitalWrite(cden, LOW);
  digitalWrite(cquat, LOW);
  // digitalWrite(cdb, LOW);
}

void setup_wifi() {
  delay(10);
  Serial.println();
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("WiFi connected");
  Serial.println(WiFi.localIP());
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect("ESP8266Client")) {
      Serial.println("connected");
      client.publish(mqtt_topic_pub, "ESP_reconnected");
      client.subscribe(mqtt_topic_sub);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  float da = dht.readHumidity();
  float nd = dht.readTemperature();
  int as = analogRead(quangtro);
  int db = 0 +random(1,10);
  if (isnan(da) || isnan(nd)) {
    Serial.println("Failed to read from DHT sensor!");
  }
  else {
 //     snprintf (msg, 50, "%d|%.2f|%.2f|%d|%d|%d", as, da, nd, den, quat, db);
      client.publish(mqtt_topic_pub, msg);
      //Serial.println(msg);
      Serial.print("Nhiet do: ");
      Serial.println(nd);               
      Serial.print("Do am: ");
      Serial.println(da);  
      Serial.print("Anh sang: ");
      Serial.println(as);        
      delay(1000);
      // if(db >= 7) {
      //   digitalWrite(cdb, HIGH);
      //   delay(200);
      //   digitalWrite(cdb, LOW);
      //   delay(200);
      //   digitalWrite(cdb, HIGH);
      //   delay(200);
      //   digitalWrite(cdb, LOW);
      //   delay(1400);
      // }
      // else{
      //   delay(2000);
      // }
  }
}