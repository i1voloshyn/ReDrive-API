package dev.ivanvoloshyn.redriveapi.vehicle.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Year;

public record VehicleRequest(
        @NotBlank
        String brand,
        @NotBlank
        String model,
        @NotNull
        VehicleType type,
        @NotNull
        @PastOrPresent
        Year productionYear,
        @NotNull
        @PositiveOrZero
        Integer initialOdometerValue
) {

}

