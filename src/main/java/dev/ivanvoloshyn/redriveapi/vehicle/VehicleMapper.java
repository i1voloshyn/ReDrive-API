package dev.ivanvoloshyn.redriveapi.vehicle;

import dev.ivanvoloshyn.redriveapi.user.model.User;
import dev.ivanvoloshyn.redriveapi.vehicle.model.Vehicle;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleRequest;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleResponse;

import java.time.Instant;

public class VehicleMapper {
    public static Vehicle toVehicle(VehicleRequest request, User user) {
        return Vehicle.builder()
                .brand(request.brand())
                .model(request.model())
                .user(user)
                .type(request.type())
                .initialOdometerValue(request.initialOdometerValue())
                .productionYear(request.productionYear())
                .createdAt(Instant.now())
                .build();
    }

    public static VehicleResponse toVehicleResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .type(vehicle.getType())
                .initialOdometerValue(vehicle.getInitialOdometerValue())
                .productionYear(vehicle.getProductionYear())
                .createdAt(vehicle.getCreatedAt())
                .build();
    }

}
