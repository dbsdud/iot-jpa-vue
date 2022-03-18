package io.dbsdud.backend.devices.domain;

import io.dbsdud.backend.common.model.BaseTimeEntity;
import io.dbsdud.backend.devices.dto.DeviceDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "device")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceId")
    private Long deviceId;

    @Column(name = "deviceName")
    private String deviceName;

    private LocalDateTime updatedAt;

    @Builder
    public Device(String deviceName) {
        this.deviceName = deviceName;
    }

    public void updDevice(DeviceDTO.UpdateRequest dto) {
        this.deviceName = dto.getDeviceName();
    }
}
