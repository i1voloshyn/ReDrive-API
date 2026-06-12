package dev.ivanvoloshyn.redriveapi.vehicle;

import dev.ivanvoloshyn.redriveapi.vehicle.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findAllByUser_Id(Long userId);
}
