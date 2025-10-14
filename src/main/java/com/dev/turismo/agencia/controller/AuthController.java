package com.dev.turismo.agencia.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.turismo.agencia.dto.LoginDTO;
import com.dev.turismo.agencia.model.Vendedor;
import com.dev.turismo.agencia.repository.VendedorRepository;
import com.dev.turismo.agencia.security.SessionRegistry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final VendedorRepository repo;
    private final SessionRegistry registry;

    public AuthController(VendedorRepository repo, SessionRegistry registry) {
        this.repo = repo;
        this.registry = registry;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto, HttpServletRequest req) {
        return repo.findByEmailAndSenha(dto.getEmail(), dto.getSenha())
                .<ResponseEntity<?>>map(v -> {
                    HttpSession session = req.getSession(true); // cria
                    session.setAttribute("VENDEDOR_ID", v.getId());
                    registry.onLogin(session, v.getId(), v.getNome()); // <<< importante
                    return ResponseEntity.ok(Map.of("id", v.getId(), "nome", v.getNome(), "email", v.getEmail()));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("erro", "Credenciais inválidas")));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> me(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("VENDEDOR_ID") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("erro", "não autenticado"));
        }
        Long id = (Long) s.getAttribute("VENDEDOR_ID");
        Vendedor v = repo.findById(id).orElse(null);
        if (v == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("erro", "sessão inválida"));
        }
        return ResponseEntity.ok(Map.of("id", v.getId(), "nome", v.getNome(), "email", v.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null)
            s.invalidate();
        return ResponseEntity.noContent().build(); // 204
    }

}
