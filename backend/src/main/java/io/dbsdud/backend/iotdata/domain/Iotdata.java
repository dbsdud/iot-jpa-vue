package io.dbsdud.backend.iotdata.domain;

import io.dbsdud.backend.common.model.BaseTimeEntity;
import io.dbsdud.backend.devices.domain.Device;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "iotdata")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Iotdata extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iotdataId")
    private Long iotdataId;

    @Column(name = "temperature")
    private String temperature;

    @Column(name = "humidity")
    private String humidity;

    @Column(name = "soilTemperature")
    private String soilTemperature;

    @Column(name = "soilHumidity")
    private String soilHumidity;

    @Column(name = "lux")
    private String lux;

    @ManyToOne
    @JoinColumn(name = "deviceId")
    private Device device;

    @Builder
    public Iotdata(String temperature, String humidity, String soilTemperature, String soilHumidity, String lux, Device device) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.soilTemperature = soilTemperature;
        this.soilHumidity = soilHumidity;
        this.lux = lux;
        this.device = device;
    }
}
