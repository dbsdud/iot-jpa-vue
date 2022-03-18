package io.dbsdud.backend.iotdata.service;

import io.dbsdud.backend.devices.domain.Device;
import io.dbsdud.backend.iotdata.domain.Iotdata;
import io.dbsdud.backend.iotdata.dto.IotdataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IotdataService {

    Iotdata registerData(IotdataDTO.DataRegisterRequest dto);

    Page<Iotdata> findAll(Pageable pageable);

    List<Iotdata> findByDevice(Device device);

    List<Iotdata> findFirst20ByDeviceOrderByIotdataIdDesc(Device device);

    Iotdata findFirst1ByDeviceOrderByIotdataIdDesc(Device device);

    void getMqttData(String payload);
}
