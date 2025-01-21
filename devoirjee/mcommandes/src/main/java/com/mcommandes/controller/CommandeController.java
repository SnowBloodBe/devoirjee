package com.mcommandes.controller;

import com.mcommandes.client.ProduitClient;
import com.mcommandes.client.ProduitServiceResilienceWrapper;
import com.mcommandes.model.Commande;
import com.mcommandes.repository.CommandeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/commandes")
public class CommandeController {
    
    @Autowired
    private final CommandeRepository commandeRepository;
    
    @Autowired
    private final ProduitClient produitClient;

    private final ProduitServiceResilienceWrapper produitServiceResilienceWrapper;


    @Value("${mes-config-ms.commandes-last}")
    private int commandesLast;

    public CommandeController(CommandeRepository commandeRepository, ProduitClient produitClient, ProduitServiceResilienceWrapper produitServiceResilienceWrapper) {
        this.commandeRepository = commandeRepository;
        this.produitClient = produitClient;
        this.produitServiceResilienceWrapper = produitServiceResilienceWrapper;

    }

    @GetMapping("/commandes/recent")
    public List<Commande> getRecentCommandes() {
        LocalDate startDate = LocalDate.now().minusDays(commandesLast);
        return commandeRepository.findRecentCommandes(startDate);
    }
    
    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @PostMapping
    public Commande createCommande(@RequestBody Commande commande) {
        if (!produitClient.checkProduitExists(commande.getIdProduit())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produit non trouvé, commande refusée");
        }
        return commandeRepository.save(commande);
    }

    @GetMapping("/{id}")
    public Commande getCommandeById(@PathVariable Long id) {
        return commandeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande not found"));
    }

    @PutMapping("/{id}")
    public Commande updateCommande(@PathVariable Long id, @RequestBody Commande updatedCommande) {
        Commande existingCommande = commandeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande not found"));
        existingCommande.setDescription(updatedCommande.getDescription());
        existingCommande.setQuantite(updatedCommande.getQuantite());
        existingCommande.setDate(updatedCommande.getDate());
        existingCommande.setMontant(updatedCommande.getMontant());
        return commandeRepository.save(existingCommande);
    }

    @DeleteMapping("/{id}")
    public void deleteCommande(@PathVariable Long id) {
        commandeRepository.deleteById(id);
    }

    @GetMapping("/test-resilience/{id}")
    @CircuitBreaker(name = "produitService", fallbackMethod = "fallbackProduitService")
    @Retry(name = "produitService", fallbackMethod = "fallbackProduitService")
    public String testResilience(@PathVariable Long id) {
    	return produitServiceResilienceWrapper.callProduitServiceWithTimeout(id);
    }

    public String fallbackProduitService(Long id, Throwable throwable) {
        return "Le service Produit est indisponible pour le produit ID: " + id + ". Veuillez réessayer plus tard. Erreur: " + throwable.getMessage();
    }

}
