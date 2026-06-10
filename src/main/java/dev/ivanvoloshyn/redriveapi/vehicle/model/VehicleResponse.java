package dev.ivanvoloshyn.redriveapi.vehicle.model;

import lombok.Builder;

import java.time.Instant;
import java.time.Year;

@Builder
public record VehicleResponse(
        int id,
        String brand,
        String model,
        VehicleType type,
        Year productionYear,
        int initialOdometerValue,
        Instant createdAt
) {
}
