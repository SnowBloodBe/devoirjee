package com.mcommandes.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@RestController
public class DynamicPropertyController {

    @Value("${mes-config-ms.commandes-last}")
    private int commandesLast;

    @GetMapping("/config/commandes-last")
    public String getCommandesLast() {
        return "Commandes Last Config: " + commandesLast;
    }
}
