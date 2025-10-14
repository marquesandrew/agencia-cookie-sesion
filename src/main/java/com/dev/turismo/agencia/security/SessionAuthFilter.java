package com.dev.turismo.agencia.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SessionAuthFilter extends OncePerRequestFilter {

    private final SessionRegistry registry;

    public SessionAuthFilter(SessionRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        // não filtra recursos que não são /api/ e o endpoint de login
        if (!p.startsWith("/api/"))
            return true;
        return p.equals("/api/login"); // adicione aqui outras rotas públicas se tiver
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        HttpSession s = req.getSession(false); // NUNCA criar aqui

        boolean autenticado = (s != null && s.getAttribute("VENDEDOR_ID") != null);
        if (!autenticado) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json; charset=UTF-8");
            res.getWriter().write("{\"erro\":\"não autenticado\"}");
            return;
        }

        // Só aqui é seguro "tocar" a sessão
        registry.onTouch(s, req);

        chain.doFilter(req, res);
    }
}