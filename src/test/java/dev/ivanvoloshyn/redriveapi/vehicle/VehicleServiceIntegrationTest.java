package dev.ivanvoloshyn.redriveapi.vehicle;

import dev.ivanvoloshyn.redriveapi.user.UserMapper;
import dev.ivanvoloshyn.redriveapi.user.UserRepository;
import dev.ivanvoloshyn.redriveapi.user.model.RegisterUserRequest;
import dev.ivanvoloshyn.redriveapi.user.model.User;
import dev.ivanvoloshyn.redriveapi.vehicle.model.Vehicle;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleRequest;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleResponse;
import dev.ivanvoloshyn.redriveapi.vehicle.model.VehicleType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@Transactional
@SpringBootTest
public class VehicleServiceIntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgresContainer = new PostgreSQLContainer(
            DockerImageName.parse("postgres:18.4")
    );

    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void createVehicle_shouldPersistVehicle() {
        User savedUser = userRepository.save(createValidUser());
        VehicleRequest vehicleRequest = vehicleRequest();

        VehicleResponse vehicleResponse = vehicleService.createVehicle(savedUser.getId(), vehicleRequest);

        entityManager.flush();
        entityManager.clear();

        Vehicle persistedVehicle = vehicleRepository.findById(vehicleResponse.id()).orElseThrow();

        assertThat(persistedVehicle.getCreatedAt()).isNotNull();
        assertThat(persistedVehicle.getUser().getId()).isEqualTo(savedUser.getId());

        assertThat(persistedVehicle.getBrand()).isEqualTo(vehicleRequest.brand());
        assertThat(persistedVehicle.getModel()).isEqualTo(vehicleRequest.model());
        assertThat(persistedVehicle.getType()).isEqualTo(vehicleRequest.type());
        assertThat(persistedVehicle.getProductionYear()).isEqualTo(vehicleRequest.productionYear());
        assertThat(persistedVehicle.getInitialOdometerValue()).isEqualTo(vehicleRequest.initialOdometerValue());
    }

    private User createValidUser() {
        String passwordHash = "passwordHash";
        RegisterUserRequest request = new RegisterUserRequest(
                "real_user@gmail.com",
                "Real",
                "User",
                "passwor"
        );
        return UserMapper.toUser(request, passwordHash);
    }

    private VehicleRequest vehicleRequest() {
        return new VehicleRequest(
                "Seat", "Leon 1P1", VehicleType.CAR, Year.of(2005),
                123456
        );
    }
}
