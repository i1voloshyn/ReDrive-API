package dev.ivanvoloshyn.redriveapi.vehicle.model;

import dev.ivanvoloshyn.redriveapi.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

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
    @CreationTimestamp
    private Instant createdAt;
}
