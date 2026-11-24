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
@Table(name = "plots")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Plot {

    @Id
    @Column(name = "plot_id", nullable = false, length = 36, columnDefinition = "CHAR(36)")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "plot_name", nullable = false, length = 150)
    private String name;

    @PrePersist
    private void assignId() {
        if (id == null) id = UUID.randomUUID().toString();
    }
}
