package io.dbsdud.backend.devices.store;

import io.dbsdud.backend.devices.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DeviceRepository extends JpaRepository<Device, Long>, QuerydslPredicateExecutor<Device> {
}
