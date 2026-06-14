package dev.ivanvoloshyn.redriveapi.exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(Long vehicleId, Long userId) {
        super("Vehicle with id " + vehicleId + " and user id " + userId + " not found");
    }
}
