package ru.otus.homework.library.actuators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class MyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        double chance = ThreadLocalRandom.current().nextDouble();
        Health.Builder status = Health.up().withDetail("message", "Потоки в порядке!");
        if (chance > 0.9) {
            status = Health.down().withDetail("message", "Что-то пошло не так с потоками!");
        }
        return status.build();
    }
}
