package io.dbsdud.backend.devices.service;

import io.dbsdud.backend.devices.domain.Device;
import io.dbsdud.backend.devices.dto.DeviceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeviceService {

    Device registerDevice(DeviceDTO.DeviceRegisterRequest dto);

    Page<Device> findAll(Pageable pageable);

    Device findById(Long deviceId);

    Device updateDevice(Long deviceId, DeviceDTO.UpdateRequest dto);
}
