package dev.ivanvoloshyn.redriveapi.vehicle;

import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleRequest;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users/{userId}/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    @NonNull
    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponse> addVehicle(@PathVariable Long userId,
                                                      @Valid @RequestBody VehicleRequest vehicleRequest) {
        VehicleResponse response = vehicleService.createVehicle(userId, vehicleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAllVehicles(@PathVariable Long userId) {
        List<VehicleResponse> vehicles = vehicleService.getUserVehicles(userId);
        return ResponseEntity.ok(vehicles);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long userId, @PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(userId, vehicleId);
        return ResponseEntity.noContent().build();
    }

}
