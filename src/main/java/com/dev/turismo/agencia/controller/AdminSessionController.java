package com.dev.turismo.agencia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dev.turismo.agencia.security.SessionRegistry;

@RestController
@RequestMapping("/api/admin")
public class AdminSessionController {
    private final SessionRegistry registry;

    public AdminSessionController(SessionRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/sessoes")
    public Object listar() {
        return registry.list();
    }

    @DeleteMapping("/sessoes/{id}")
    public ResponseEntity<?> encerrar(@PathVariable String id) {
        boolean ok = registry.invalidate(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}