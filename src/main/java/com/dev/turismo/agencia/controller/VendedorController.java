package com.dev.turismo.agencia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.turismo.agencia.model.Vendedor;
import com.dev.turismo.agencia.repository.VendedorRepository;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    private final VendedorRepository repo;
    
    public VendedorController(VendedorRepository repo) {
        this.repo = repo;
    }
    
    @GetMapping
    public List<Vendedor> listar() {        
        return repo.findAll();
    }
  
    @GetMapping("/{id}")
    public Vendedor buscar(@RequestParam Long id) {
        return repo.findById(id).orElse(null);
    }

    @PostMapping
    public Vendedor criar(@RequestBody Vendedor vendedor) {
        vendedor.setId(null); // Garantir que o ID seja nulo para criação
        return repo.save(vendedor);
    }

    @PutMapping("/{id}")
    public Vendedor atualizar(@PathVariable Long id, @RequestBody Vendedor vendedor) {
        vendedor.setId(id);
        return repo.save(vendedor);
    }

    @DeleteMapping("/{id}")
    public Vendedor apagar(@PathVariable Long id) {
        repo.deleteById(id);
        return null;
    }
    
}
