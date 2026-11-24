CREATE DATABASE IF NOT EXISTS iot;
USE iot;

CREATE TABLE IF NOT EXISTS gateways (
    id BIGINT NOT NULL AUTO_INCREMENT,
    gateway_code VARCHAR(100) NOT NULL,
    name VARCHAR(150) NOT NULL,
    status VARCHAR(30) NOT NULL,
    last_seen_at DATETIME(6) NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_gateways_gateway_code (gateway_code)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sensor_nodes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    gateway_id BIGINT NOT NULL,
    node_code VARCHAR(100) NOT NULL,
    name VARCHAR(150) NOT NULL,
    status VARCHAR(30) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_node_gateway_code (gateway_id, node_code),
    CONSTRAINT fk_sensor_nodes_gateway FOREIGN KEY (gateway_id) REFERENCES gateways(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sensors (
    sensor_id CHAR(36) NOT NULL,
    sensor_node_id BIGINT NOT NULL,
    sensor_code VARCHAR(100) NOT NULL,
    type VARCHAR(100) NOT NULL,
    unit VARCHAR(50) NULL,
    enabled BOOLEAN NOT NULL,
    PRIMARY KEY (sensor_id),
    UNIQUE KEY uk_sensor_node_code (sensor_node_id, sensor_code),
    CONSTRAINT fk_sensors_node FOREIGN KEY (sensor_node_id) REFERENCES sensor_nodes(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS plots (
    plot_id CHAR(36) NOT NULL,
    plot_name VARCHAR(150) NOT NULL,
    PRIMARY KEY (plot_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sensor_configs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sensor_id CHAR(36) NOT NULL,
    read_interval_seconds INT NOT NULL,
    min_threshold DOUBLE NULL,
    max_threshold DOUBLE NULL,
    enabled BOOLEAN NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_sensor_configs_sensor_id (sensor_id),
    CONSTRAINT fk_sensor_configs_sensor FOREIGN KEY (sensor_id) REFERENCES sensors(sensor_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sensor_readings (
    reading_id BIGINT NOT NULL AUTO_INCREMENT,
    sensor_id CHAR(36) NOT NULL,
    `timestamp` TIMESTAMP(6) NOT NULL,
    plot_id CHAR(36) NOT NULL,
    pH DECIMAL(5,2) NULL,
    ec DECIMAL(10,3) NULL,
    soil_moisture DECIMAL(8,3) NULL,
    n DECIMAL(12,3) NULL,
    p DECIMAL(12,3) NULL,
    k DECIMAL(12,3) NULL,
    quality_status VARCHAR(50) NOT NULL,
    PRIMARY KEY (reading_id),
    KEY idx_reading_sensor_timestamp (sensor_id, `timestamp`),
    CONSTRAINT fk_sensor_readings_sensor FOREIGN KEY (sensor_id) REFERENCES sensors(sensor_id),
    CONSTRAINT fk_sensor_readings_plot FOREIGN KEY (plot_id) REFERENCES plots(plot_id)
) ENGINE=InnoDB;
