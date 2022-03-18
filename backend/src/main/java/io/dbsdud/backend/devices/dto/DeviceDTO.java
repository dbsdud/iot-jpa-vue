package io.dbsdud.backend.devices.dto;

import io.dbsdud.backend.devices.domain.Device;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class DeviceDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeviceRegisterRequest {
        private String deviceName;

        @Builder
        DeviceRegisterRequest(String deviceName) {
            this.deviceName = deviceName;
        }

        public Device toEntity() {
            return Device.builder()
                    .deviceName(this.deviceName)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeviceResponse {
        private Long deviceId;
        private String deviceName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public DeviceResponse(Device device) {
            this.deviceId = device.getDeviceId();
            this.deviceName = device.getDeviceName();
            this.createdAt = device.getCreatedAt();
            this.updatedAt = device.getUpdatedAt();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRequest {

        private String deviceName;

        @Builder
        public UpdateRequest(final Device device) {
            this.deviceName = device.getDeviceName();
            // this.updatedAt = updatedAt;
        }
    }
}
