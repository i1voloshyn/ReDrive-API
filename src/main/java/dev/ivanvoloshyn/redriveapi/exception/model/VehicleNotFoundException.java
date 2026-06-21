package dev.ivanvoloshyn.redriveapi.exception.model;

import org.springframework.http.HttpStatus;

public class VehicleNotFoundException extends ApiException {
    public VehicleNotFoundException(Long vehicleId, Long userId) {
        super("Vehicle with id " + vehicleId + " and user id " + userId + " not found", HttpStatus.NOT_FOUND);
    }
}
