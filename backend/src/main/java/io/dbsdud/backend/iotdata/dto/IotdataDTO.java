package io.dbsdud.backend.iotdata.dto;

import io.dbsdud.backend.devices.domain.Device;
import io.dbsdud.backend.iotdata.domain.Iotdata;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class IotdataDTO {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DataRegisterRequest {
        private String temperature;
        private String humidity;
        private String soilTemperature;
        private String soilHumidity;
        private String lux;

        private Device device;
        private Long deviceId;

        @Builder
        public DataRegisterRequest(String temperature, String humidity, String soilTemperature, String soilHumidity, String lux, Device device) {
            this.temperature = temperature;
            this.humidity = humidity;
            this.soilTemperature = soilTemperature;
            this.soilHumidity = soilHumidity;
            this.lux = lux;
            this.device = device;
        }

        public Iotdata toEntity() {
            return Iotdata.builder()
                    .temperature(this.temperature)
                    .humidity(this.humidity)
                    .soilTemperature(this.soilTemperature)
                    .soilHumidity(this.soilHumidity)
                    .lux(this.lux)
                    .device(this.device)
                    .build();
        }
    }

    @Getter
    public static class DataResponse {
        private final String temperature;
        private final String humidity;
        private final String soilTemperature;
        private final String soilHumidity;
        private final String lux;
        private final Device device;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public DataResponse(Iotdata data) {
            this.temperature = data.getTemperature();
            this.humidity = data.getHumidity();
            this.soilTemperature = data.getSoilTemperature();
            this.soilHumidity = data.getHumidity();
            this.lux = data.getLux();
            this.device = data.getDevice();
            this.createdAt = data.getCreatedAt();
            this.updatedAt = data.getUpdatedAt();
        }
    }
}
