package dev.ivanvoloshyn.redriveapi.vehicle.model;

import dev.ivanvoloshyn.redriveapi.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.Year;

@Entity
@Table(name = "vehicle")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter()
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String brand;
    private String model;
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    private Year productionYear;
    private Integer initialOdometerValue;
    private Instant createdAt;
}
