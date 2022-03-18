package io.dbsdud.backend.iotdata.store;

import io.dbsdud.backend.devices.domain.Device;
import io.dbsdud.backend.iotdata.domain.Iotdata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface IotdataRepository extends JpaRepository<Iotdata, Long>, QuerydslPredicateExecutor<Iotdata> {

    List<Iotdata> findByDevice(Device device);

    List<Iotdata> findFirst20ByDeviceOrderByIotdataIdDesc(Device device);

    Iotdata findFirst1ByDeviceOrderByIotdataIdDesc(Device device);

}
