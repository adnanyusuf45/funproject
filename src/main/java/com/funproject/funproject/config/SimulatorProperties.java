package com.funproject.funproject.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "simulator")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulatorProperties {
    private String negative_phone_number;
}
