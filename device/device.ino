#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <OneWire.h>
#include <DHT.h>
#include <DHT_U.h>

const char* ssid = "WiFi ssid";  // Wifi name
const char* password = "WiFi password";  // Wifi Password
const char* mqtt_server = "MQTT Server's IP";

long deviceId = 1; // FIXME
float temperature, humidity, soilTemperature, soilHumidity, lux;

String fullTopic = "devices/" + String(deviceId);
const char* topic = fullTopic.c_str();

WiFiClient espClient;
PubSubClient client(espClient);
unsigned long lastMsg = 0;
#define MSG_BUFFER_SIZE (200)
char msg[MSG_BUFFER_SIZE];
int value = 0;

// Mux control pins
int s0 = D8;
int s1 = D7;
int s2 = D6;
int s3 = D5;

// Mux int "SIG" pin
int SIG_pin = A0;

// soilTemperature
int DS18S20_Pin = 5;
OneWire ds(DS18S20_Pin);

#define DHTPIN 4        // SDA 핀의 설정
#define DHTTYPE DHT22   // DHT22 (AM2302) 센서종류 설정

DHT dht(DHTPIN, DHTTYPE);

void setup_wifi() {
  delay(10);

  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived[");
  Serial.print(topic);
  Serial.print("]");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();

  if ((char)payload[0] == '1') {
    digitalWrite(BUILTIN_LED, LOW);
  } else {
    digitalWrite(BUILTIN_LED, HIGH);
  }
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      client.publish("outTopic", "hello world");
      client.subscribe("inTopic");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try agin in 5 seconds");

      delay(10000);
    }
  }
}

float getTemp() {                                  //온도 측정 후 반환하는 함수
  byte data[12];
  byte addr[8];
  if ( !ds.search(addr)) {
    ds.reset_search();
    return -1000;
  }
  if ( OneWire::crc8( addr, 7) != addr[7]) {
    Serial.println("CRC is not valid!");
    return -1000;
  }
  if ( addr[0] != 0x10 && addr[0] != 0x28) {
    Serial.print("Device is not recognized");
    return -1000;
  }
  ds.reset();
  ds.select(addr);
  ds.write(0x44, 1);
  byte present = ds.reset();
  ds.select(addr);
  ds.write(0xBE);

  for (int i = 0; i < 9; i++) {
    data[i] = ds.read();
  }

  ds.reset_search();
  byte MSB = data[1];
  byte LSB = data[0];
  float tempRead = ((MSB << 8) | LSB);
  float TemperatureSum = tempRead / 16;
  return TemperatureSum;
}

String json_data(long deviceId, float humidity, float temperature, float soilHumidity, float soilTemperature, float lux) {
  String s_id = String(deviceId);
  String s_hum = String(humidity);
  String s_tem = String(temperature);
  String s_soilH = String(soilHumidity);
  String s_soilT = String(soilTemperature);
  String s_lux = String(lux);
  String str = "{\"device\": {\"deviceId\": \"" + s_id + "\"}, \"humidity\": \"" + s_hum + "\", \"temperature\": \"" + s_tem + "\", \"soilHumidity\": \"" + s_soilH + "\", \"soilTemperature\": \"" + s_soilT + "\", \"lux\": \"" + s_lux + "\"}";
  return str;
}

float calc_soilH() {
  soilHumidity = 100 - map(getAnalog(1), 0, 1023, 0, 100);
  return soilHumidity;
}

float calc_lux() {
  lux = 100 - map(getAnalog(3), 0, 1023, 0, 100);
  return lux;
}

void setup() {
  // put your setup code here, to run once:
  pinMode(BUILTIN_LED, OUTPUT);

  pinMode(SIG_pin, INPUT);

  // Start Mux Code
  pinMode(s0, OUTPUT);
  pinMode(s1, OUTPUT);
  pinMode(s2, OUTPUT);
  pinMode(s3, OUTPUT);

  // End Mux Code
  Serial.begin(115200); // 보드레이트 속도
  dht.begin();

  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}

void loop() {
  // put your main code here, to run repeatedly:
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  humidity = dht.readHumidity();
  temperature = dht.readTemperature();

  calc_soilH();

  soilTemperature = getTemp();

  calc_lux();

  ++value;
  snprintf (msg, MSG_BUFFER_SIZE, json_data(deviceId, humidity, temperature, soilHumidity, soilTemperature, lux).c_str(), value);

  client.publish(topic, msg);
  Serial.println(msg);

  delay(10000);
}

float getAnalog(int MUXyPin) {
  digitalWrite(s3, HIGH && (MUXyPin & B00001000));
  digitalWrite(s2, HIGH && (MUXyPin & B00000100));
  digitalWrite(s1, HIGH && (MUXyPin & B00000010));
  digitalWrite(s0, HIGH && (MUXyPin & B00000001));
  return (float)analogRead(SIG_pin);
}
