package io.dbsdud.backend.devices.controller;

import io.dbsdud.backend.common.model.PageRequest;
import io.dbsdud.backend.devices.dto.DeviceDTO;
import io.dbsdud.backend.devices.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    // 장치 등록
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public DeviceDTO.DeviceResponse registerDevice(@RequestBody @Valid final DeviceDTO.DeviceRegisterRequest dto) {
        return new DeviceDTO.DeviceResponse(deviceService.registerDevice(dto));
    }

    // 장치 목록 조회
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<DeviceDTO.DeviceResponse> getDevices(final PageRequest pageable) {
        return deviceService.findAll(pageable.of()).map(DeviceDTO.DeviceResponse::new);
    }

    // 장치 상세 조회
    @GetMapping("/{device_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public DeviceDTO.DeviceResponse getDeviceDetail(@PathVariable final Long device_id) {
        return new DeviceDTO.DeviceResponse(deviceService.findById(device_id));
    }

    // 장치 정보 수정
    @PutMapping("/{device_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public DeviceDTO.UpdateRequest updDevice(@PathVariable final Long device_id, @RequestBody final DeviceDTO.UpdateRequest dto) {
        return new DeviceDTO.UpdateRequest(deviceService.updateDevice(device_id, dto));
    }
}
