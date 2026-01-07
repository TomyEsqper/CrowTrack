package com.tuplataforma.core.infrastructure.startup;

import com.tuplataforma.core.infrastructure.persistence.fleet.JpaVehicleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupValidator {
    @Bean
    public ApplicationRunner validate(JpaVehicleRepository vehicleRepo) {
        return args -> {
            vehicleRepo.count();
        };
    }
}

