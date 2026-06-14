package dev.ivanvoloshyn.redriveapi.vehicle;

import dev.ivanvoloshyn.redriveapi.exception.auth.UserNotFoundException;
import dev.ivanvoloshyn.redriveapi.exception.VehicleNotFoundException;
import dev.ivanvoloshyn.redriveapi.user.UserRepository;
import dev.ivanvoloshyn.redriveapi.user.model.User;
import dev.ivanvoloshyn.redriveapi.vehicle.model.Vehicle;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleRequest;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleResponse createVehicle(Long userId, VehicleRequest vehicleRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Vehicle vehicle = VehicleMapper.toVehicle(vehicleRequest, user);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return VehicleMapper.toVehicleResponse(savedVehicle);
    }

    public List<VehicleResponse> getUserVehicles(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return vehicleRepository.findAllByUser_Id(userId).stream()
                .map(VehicleMapper::toVehicleResponse)
                .toList();
    }

    public void deleteVehicle(Long userId, Long vehicleId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        Vehicle vehicle = vehicleRepository.findByIdAndUser_Id(vehicleId, userId)
                .orElseThrow(() -> new VehicleNotFoundException(vehicleId, userId));
        vehicleRepository.delete(vehicle);
    }

}
