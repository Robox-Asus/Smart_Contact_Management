package com.managment.helpers;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Controller;

@Controller
public class DBHealthService implements HealthIndicator {

	// add your db service logic , it is just for testing custom actuator
	private final static String DB_Serive = "Database Service";

	public boolean isHealthGood() {
		return true;
	}

	@Override
	public Health health() {
		// TODO Auto-generated method stub

		if (isHealthGood()) {
			return Health.up().withDetail(DB_Serive, "Database service is running ....").build();
		}

		return Health.down().withDetail(DB_Serive, "Database service is down ...").build();
	}

}
