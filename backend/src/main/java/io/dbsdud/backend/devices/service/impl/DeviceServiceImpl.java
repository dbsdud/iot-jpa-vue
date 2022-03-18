package io.dbsdud.backend.devices.service.impl;

import io.dbsdud.backend.devices.domain.Device;
import io.dbsdud.backend.devices.dto.DeviceDTO;
import io.dbsdud.backend.devices.service.DeviceService;
import io.dbsdud.backend.devices.store.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    // 장치 등록
    public Device registerDevice(DeviceDTO.DeviceRegisterRequest dto) {
        // TODO: 예외처리
        return deviceRepository.save(dto.toEntity());
    }

    // 장치 목록 조회
    @Transactional(readOnly = true)
    public Page<Device> findAll(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }

    // 장치 상세 조회
    @Transactional(readOnly = true)
    public Device findById(Long deviceId) {
        final Optional<Device> device = deviceRepository.findById(deviceId);
        return device.get();
    }

    // 장치 정보 수정
    public Device updateDevice(Long deviceId, DeviceDTO.UpdateRequest dto) {
        final Device device = findById(deviceId);
        device.updDevice(dto);
        return device;
    }
}
