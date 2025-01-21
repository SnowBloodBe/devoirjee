package com.mcommandes.health;

import com.mcommandes.repository.CommandeRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CommandeHealthIndicator implements HealthIndicator {

    private final CommandeRepository commandeRepository;

    public CommandeHealthIndicator(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    @Override
    public Health health() {
        long recordCount = commandeRepository.count();
        if (recordCount > 0) {
            return Health.up()
                         .withDetail("status", "UP")
                         .withDetail("message", "Commandes exist in the database")
                         .withDetail("commande-count", recordCount)
                         .build();
        } else {
            return Health.down()
                         .withDetail("status", "DOWN")
                         .withDetail("message", "No commandes found in the database")
                         .build();
        }
    }
}
