package com.traelo.delivery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {
	// Enables scheduled tasks (to automatically expire trials)
}
