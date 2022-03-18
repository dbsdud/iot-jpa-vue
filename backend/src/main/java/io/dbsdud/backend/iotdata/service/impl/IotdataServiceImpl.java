package io.dbsdud.backend.iotdata.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dbsdud.backend.devices.domain.Device;
import io.dbsdud.backend.iotdata.domain.Iotdata;
import io.dbsdud.backend.iotdata.dto.IotdataDTO;
import io.dbsdud.backend.iotdata.service.IotdataService;
import io.dbsdud.backend.iotdata.store.IotdataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class IotdataServiceImpl implements IotdataService {

    private final IotdataRepository iotdataRepository;

    public Iotdata registerData(IotdataDTO.DataRegisterRequest dto) {
        return iotdataRepository.save(dto.toEntity());
    }

    @Transactional(readOnly = true)
    public Page<Iotdata> findAll(Pageable pageable) {
        return iotdataRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Iotdata> findByDevice(final Device device) {
        return iotdataRepository.findByDevice(device);
    }

    @Transactional(readOnly = true)
    public List<Iotdata> findFirst20ByDeviceOrderByIotdataIdDesc(final Device device) {
        return iotdataRepository.findFirst20ByDeviceOrderByIotdataIdDesc(device);
    }

    @Transactional(readOnly = true)
    public Iotdata findFirst1ByDeviceOrderByIotdataIdDesc(final Device device) {
        return iotdataRepository.findFirst1ByDeviceOrderByIotdataIdDesc(device);
    }

    @Transactional(readOnly = true)
    public void getMqttData(String payload) {
        System.out.println("Service get:" + payload);
        payload = payload.replace("'","\"");
        ObjectMapper mapper = new ObjectMapper();

        try {
            IotdataDTO.DataRegisterRequest dto = mapper.readValue(payload, IotdataDTO.DataRegisterRequest.class);
            System.out.println(dto);
            iotdataRepository.save(dto.toEntity());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
