package com.example.crudgame_z.config;

import com.example.crudgame_z.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// Filtro para autenticar peticiones usando JWT
// OncePerRequestFilter asegura que el filtro se ejecute una vez por petición
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Inyectar dependencias, JwtTokenProvider permite validar y extraer info del token
    // CustomUserDetailsService carga detalles del usuario desde la base de datos
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    // metodo que se ejecuta por cada petición HTTP entrante
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

                // Obtener el token JWT de la cabecera "Authorization"
        String authHeader = request.getHeader("Authorization");

        // Validar el token que empieza con "Bearer"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extraer el token quitando el prefijo "Bearer"
            String token = authHeader.substring(7);

            // Validar el token comprobando su integridad y expiración
            if (jwtTokenProvider.validateToken(token)) {

                String email = jwtTokenProvider.getEmailFromToken(token);

                // Evitar sobrescribir autenticación existente
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // cargar detalles del usuario de la bd usando el email del token
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    // Crear objeto de autenticación con los detalles del usuario
                    UsernamePasswordAuthenticationToken authToken =
                    // guardar la autenticación en el contexto de seguridad de Spring como la ip y session
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Guardar la autenticación en el contexto de seguridad de Spring
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

            // Continuar con la cadena de filtros, permitiendo que la petición llegue al controlador
            // Es importante llamar a este método para que la petición no se bloquee
        filterChain.doFilter(request, response);
    }
}
