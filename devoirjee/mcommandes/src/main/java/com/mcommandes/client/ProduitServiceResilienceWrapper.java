package com.mcommandes.client;

import org.springframework.stereotype.Service;
import io.github.resilience4j.timelimiter.TimeLimiter;
import java.util.concurrent.CompletableFuture;

@Service
public class ProduitServiceResilienceWrapper {

    private final ProduitClient produitClient;
    private final TimeLimiter timeLimiter;

    public ProduitServiceResilienceWrapper(ProduitClient produitClient, TimeLimiter timeLimiter) {
        this.produitClient = produitClient;
        this.timeLimiter = timeLimiter;
    }

    public String callProduitServiceWithTimeout(Long id) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> produitClient.simulateLongProcess(id));
        try {
            return timeLimiter.executeFutureSupplier(() -> future);
        } catch (Exception e) {
            return "Le service produit est trop lent, veuillez réessayer plus tard.";
        }
    }
}
