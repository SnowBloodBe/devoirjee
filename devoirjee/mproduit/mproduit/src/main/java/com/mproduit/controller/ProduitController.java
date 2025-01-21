package com.mproduit.controller;

import com.mproduit.model.Produit;
import com.mproduit.repository.ProduitRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitRepository produitRepository;

    public ProduitController(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @GetMapping
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @PostMapping
    public Produit createProduit(@RequestBody Produit produit) {
        return produitRepository.save(produit);
    }
    
    @GetMapping("/{id}")
    public boolean checkProduitExists(@PathVariable Long id) {
        return produitRepository.existsById(id);
    }

    @GetMapping("/simulate-delay/{id}")
    public String simulateLongProcess(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(5000);  // Simulation d'un délai de 5 secondes
        return "Produit " + id + " traité après un long délai.";
    }
}
