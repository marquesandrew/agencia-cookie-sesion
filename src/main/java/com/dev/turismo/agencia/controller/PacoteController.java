package com.dev.turismo.agencia.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.turismo.agencia.model.Pacote;
import com.dev.turismo.agencia.repository.PacoteRepository;

@RestController
@RequestMapping("/api/pacotes")
public class PacoteController {

    private final PacoteRepository repo;

    public PacoteController(PacoteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Pacote> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Pacote buscar(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PostMapping
    public Pacote criar(@RequestBody Pacote p) {
        p.setId(null);
        return repo.save(p);
    }

    @PutMapping("/{id}")
    public Pacote atualizar(@PathVariable Long id, @RequestBody Pacote p) {
        p.setId(id);
        return repo.save(p);
    }

    @DeleteMapping("/{id}")
    public void apagar(@PathVariable Long id) {
        repo.deleteById(id);
    }
    
}
