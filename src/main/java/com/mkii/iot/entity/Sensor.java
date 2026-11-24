package com.mkii.iot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.UUID;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sensor {

    @Id
    @Column(name = "sensor_id", nullable = false, length = 36, columnDefinition = "CHAR(36)")
    @EqualsAndHashCode.Include
    private String id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @jakarta.persistence.ManyToOne(optional = false)
    @jakarta.persistence.JoinColumn(name = "sensor_node_id", nullable = false)
    private SensorNode sensorNode;

    @Column(name = "sensor_code", nullable = false, length = 100)
    private String sensorCode;

    @Column(nullable = false, length = 100)
    private String type;

    @Column(length = 50)
    private String unit;

    @Column(nullable = false)
    private Boolean enabled;

    @PrePersist
    private void assignId() {
        if (id == null) id = UUID.randomUUID().toString();
    }
}
