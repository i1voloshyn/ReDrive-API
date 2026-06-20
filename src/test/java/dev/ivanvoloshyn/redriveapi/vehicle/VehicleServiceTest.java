package dev.ivanvoloshyn.redriveapi.vehicle;

import dev.ivanvoloshyn.redriveapi.exception.auth.UserNotFoundException;
import dev.ivanvoloshyn.redriveapi.exception.VehicleNotFoundException;
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

import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        Long vehicleId = 1L;
        Long userId = 1L;
        User user = new User(
                userId,
                "Joe",
                "Toronto",
                "test_email@email.com",
                "user_password"
        );

        VehicleRequest request = new VehicleRequest(
                "Seat", "Leon 1P1", VehicleType.CAR, Year.of(2005), 214_534
        );

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
        assertEquals(request.model(), response.model());
        assertEquals(request.type(), response.type());
    }

    @Test
    void getUserVehicles_shouldReturnAllVehicles_forCurrentUser() {
        Long userId = 1L;
        User user = new User(
                userId,
                "Joe",
                "Toronto",
                "test_email@email.com",
                "user_password"
        );
        Instant createdAt1 = Instant.parse("2026-06-11T10:00:00Z");
        Vehicle vehicle1 = Vehicle.builder()
                .id(1L)
                .brand("Seat")
                .model("Leon")
                .type(VehicleType.CAR)
                .initialOdometerValue(123456)
                .user(user)
                .createdAt(createdAt1)
                .build();

        Instant createdAt2 = Instant.parse("2026-06-10T10:00:00Z");
        Vehicle vehicle2 = Vehicle.builder()
                .id(2L)
                .brand("Volvo")
                .model("V40")
                .type(VehicleType.CAR)
                .initialOdometerValue(234567)
                .user(user)
                .createdAt(createdAt2)
                .build();

        List<Vehicle> entities = List.of(vehicle1, vehicle2);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(vehicleRepository.findAllByUser_Id(userId)).thenReturn(entities);

        List<VehicleResponse> vehicleResponses = vehicleService.getUserVehicles(userId);

        verify(userRepository).existsById(userId);
        verify(vehicleRepository).findAllByUser_Id(userId);

        assertThat(vehicleResponses)
                .extracting(
                        VehicleResponse::id,
                        VehicleResponse::brand,
                        VehicleResponse::model,
                        VehicleResponse::createdAt)
                .containsExactlyInAnyOrder(
                        tuple(1L, "Seat", "Leon", createdAt1),
                        tuple(2L, "Volvo", "V40", createdAt2));

    }

    @Test
    void getUserVehicles_shouldReturnEmptyList_whenUserHasNoVehicles() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(vehicleRepository.findAllByUser_Id(userId)).thenReturn(List.of());

        List<VehicleResponse> vehicleResponses = vehicleService.getUserVehicles(userId);

        verify(userRepository).existsById(userId);
        verify(vehicleRepository).findAllByUser_Id(userId);

        assertThat(vehicleResponses).isEmpty();
    }

    @Test
    void getUserVehicles_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> vehicleService.getUserVehicles(userId));

        verify(userRepository).existsById(userId);
        verifyNoInteractions(vehicleRepository);

        assertThat(e).hasMessage("User with Id " + userId + " not found");

    }

    @Test
    void deleteVehicle_shouldThrowVehicleNotFoundException_whenVehicleIsNotFoundForUser() {
        Long vehicleId = 1L;
        Long userId = 2L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(vehicleRepository.findByIdAndUser_Id(vehicleId, userId)).thenReturn(Optional.empty());

        VehicleNotFoundException e = assertThrows(VehicleNotFoundException.class,
                () -> vehicleService.deleteVehicle(userId, vehicleId));

        verify(userRepository).existsById(userId);
        verify(vehicleRepository).findByIdAndUser_Id(vehicleId, userId);
        verify(vehicleRepository, never()).delete(any(Vehicle.class));

        assertThat(e).hasMessage("Vehicle with id " + vehicleId + " and user id " + userId + " not found");

    }

    @Test
    void deleteVehicle_shouldDeleteVehicle_whenVehicleBelongsToUser() {
        Long vehicleId = 1L;
        Long userId = 2L;

        User user = new User(
                userId,
                "Joe",
                "Toronto",
                "test_email@email.com",
                "user_password"
        );
        Instant createdAt = Instant.parse("2026-06-11T10:00:00Z");
        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .brand("Seat")
                .model("Leon")
                .type(VehicleType.CAR)
                .initialOdometerValue(123456)
                .user(user)
                .createdAt(createdAt)
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(vehicleRepository.findByIdAndUser_Id(vehicleId, userId)).thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(userId, vehicleId);

        verify(userRepository).existsById(userId);
        verify(vehicleRepository).findByIdAndUser_Id(vehicleId, userId);
        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    void updateVehicle_shouldUpdateVehicleSuccessfully_whenVehicleBelongsToUser() {
        Long vehicleId = 1L;
        Long userId = 2L;
        Instant createdAt = Instant.parse("2026-06-11T10:00:00Z");

        User user = new User(
                userId,
                "Joe",
                "Toronto",
                "test_email@email.com",
                "user_password"
        );

        Vehicle existing = Vehicle.builder()
                .id(vehicleId)
                .user(user)
                .brand("Seat")
                .model("Leon")
                .type(VehicleType.CAR)
                .initialOdometerValue(129456)
                .productionYear(Year.of(2006))
                .createdAt(createdAt)
                .build();

        VehicleRequest request = new VehicleRequest(
                "Seat", "Leon 1P1", VehicleType.CAR,
                Year.of(2005), 123456
        );

        when(userRepository.existsById(userId)).thenReturn(true);
        when(vehicleRepository.findByIdAndUser_Id(vehicleId, userId)).thenReturn(Optional.of(existing));

        vehicleService.updateVehicle(userId, vehicleId, request);

        verify(userRepository).existsById(userId);
        verify(vehicleRepository).findByIdAndUser_Id(vehicleId, userId);

        assertEquals(existing.getBrand(), request.brand());
        assertEquals(existing.getModel(), request.model());
        assertEquals(existing.getType(), request.type());
        assertEquals(existing.getProductionYear(), request.productionYear());
        assertEquals(existing.getInitialOdometerValue(), request.initialOdometerValue());

    }

}