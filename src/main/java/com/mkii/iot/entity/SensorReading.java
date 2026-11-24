package com.mkii.iot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "sensor_readings")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reading_id")
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(name = "`timestamp`", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant timestamp;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "plot_id", nullable = false)
    private Plot plot;

    @Column(name = "pH", precision = 5, scale = 2)
    private BigDecimal ph;

    @Column(name = "ec", precision = 10, scale = 3)
    private BigDecimal ec;

    @Column(name = "soil_moisture", precision = 8, scale = 3)
    private BigDecimal soilMoisture;

    @Column(name = "n", precision = 12, scale = 3)
    private BigDecimal nitrogen;

    @Column(name = "p", precision = 12, scale = 3)
    private BigDecimal phosphorus;

    @Column(name = "k", precision = 12, scale = 3)
    private BigDecimal potassium;

    @Column(name = "quality_status", nullable = false, length = 50)
    private String qualityStatus;
}
