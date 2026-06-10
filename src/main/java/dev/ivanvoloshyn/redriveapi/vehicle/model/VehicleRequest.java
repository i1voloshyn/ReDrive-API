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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String brand;
        private String model;
        private VehicleType type;
        private Year productionYear;
        private int initialOdometerValue;

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder type(VehicleType type) {
            this.type = type;
            return this;
        }

        public Builder productionYear(Year productionYear) {
            this.productionYear = productionYear;
            return this;
        }

        public Builder initialOdometerValue(int initialOdometerValue) {
            this.initialOdometerValue = initialOdometerValue;
            return this;
        }

        public VehicleRequest build() {
            return new VehicleRequest(
                    brand,
                    model,
                    type,
                    productionYear,
                    initialOdometerValue
            );
        }

    }
}
