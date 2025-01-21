package com.mcommandes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mproduit")
public interface ProduitClient {

    @GetMapping("/produits/{id}")
    boolean checkProduitExists(@PathVariable Long id);
    
    @GetMapping("/produits/simulate-delay/{id}")
    String simulateLongProcess(@PathVariable Long id);

}
