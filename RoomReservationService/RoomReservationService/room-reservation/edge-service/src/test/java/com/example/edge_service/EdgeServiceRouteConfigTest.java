package com.example.edge_service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

class EdgeServiceRouteConfigTest {

    @Test
    void applicationConfigRoutesRoomsAndReservations() {
        Properties properties = loadYaml(new ClassPathResource("application.yml"));

        assertEdgeRoutes(properties);
    }

    @Test
    void configRepoRoutesRoomsAndReservations() {
        Properties properties = loadYaml(new FileSystemResource("../config-repo/edge-service.yml"));

        assertEdgeRoutes(properties);
    }

    private static Properties loadYaml(Resource resource) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(resource);
        return yaml.getObject();
    }

    private static void assertEdgeRoutes(Properties properties) {
        String config = properties.toString();

        assertTrue(config.contains("Path=/api/rooms/**"));
        assertTrue(config.contains("Path=/api/reservations/**"));
        assertTrue(config.contains("Path=/rooms/**"));
        assertTrue(config.contains("Path=/reservations/**"));
        assertTrue(config.contains("StripPrefix=1"));
    }
}
