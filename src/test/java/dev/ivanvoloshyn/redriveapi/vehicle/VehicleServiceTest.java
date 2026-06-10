package dev.ivanvoloshyn.redriveapi.vehicle;

import dev.ivanvoloshyn.redriveapi.user.UserRepository;
import dev.ivanvoloshyn.redriveapi.user.model.User;
import dev.ivanvoloshyn.redriveapi.vehicle.model.Vehicle;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleRequest;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleResponse;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createVehicle_shouldSave_andReturnVehicleResponse() {
        int vehicleId = 1;
        Long userId = 1L;
        User user = new User(
                userId,
                "Joe",
                "Toronto",
                "test_email@email.com",
                "user_password"
        );

        VehicleRequest request = VehicleRequest.builder()
                .brand("Seat")
                .model("Leon 1P1")
                .type(VehicleType.CAR)
                .productionYear(Year.of(2005))
                .initialOdometerValue(214_534)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> {
            Vehicle vehicleToSave = i.getArgument(0);
            vehicleToSave.setId(vehicleId);
            return vehicleToSave;
        });

        VehicleResponse response = vehicleService.createVehicle(userId, request);

        verify(userRepository).findById(userId);
        ArgumentCaptor<Vehicle> captor = forClass(Vehicle.class);
        verify(vehicleRepository).save(captor.capture());

        Vehicle savedVehicle = captor.getValue();

        //assert saved vehicle
        assertEquals(vehicleId, savedVehicle.getId());
        assertEquals(userId, savedVehicle.getUser().getId());
        assertEquals(request.brand(), savedVehicle.getBrand());
        assertEquals(request.productionYear().getValue(), savedVehicle.getProductionYear().getValue());

        //assert response
        assertEquals(savedVehicle.getId(), response.id());
        assertEquals(request.model(), savedVehicle.getModel());
        assertEquals(request.type(), savedVehicle.getType());
    }

}