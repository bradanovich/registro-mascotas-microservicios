package cl.mascotas.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static cl.mascotas.gateway.security.Constants.*;

/**
 * Filtro encargado de revisar el JWT
 * antes de permitir que la petición continúe.
 *
 * Este filtro se ejecuta una vez por cada petición.
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Obtiene el JWT desde el header Authorization
     * y extrae la información que contiene.
     */
    private Claims obtenerClaims(HttpServletRequest request) {

        // Ejemplo recibido:
        // Authorization: Bearer eyJ...
        String authorizationHeader =
                request.getHeader(HEADER_AUTHORIZATION);

        // Quitamos "Bearer " y nos quedamos solamente con el JWT.
        String jwtToken =
                authorizationHeader.replace(TOKEN_BEARER_PREFIX, "");

        /*
         * Validamos la firma utilizando exactamente
         * la misma clave secreta usada por Usuarios MS.
         */
        return Jwts.parser()
                .verifyWith(
                        (SecretKey) getSigningKey(SUPER_SECRET_KEY)
                )
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    /**
     * Si el token es válido, registramos al usuario
     * como autenticado dentro de Spring Security.
     */
    private void establecerAutenticacion(Claims claims) {

        /*
         * Recuperamos los permisos que Usuarios MS
         * guardó dentro del JWT.
         *
         * En nuestro caso:
         * ROLE_USER
         */
        List<String> authorities =
                (List<String>) claims.get("authorities");

        /*
         * Creamos la autenticación que Spring Security
         * guardará durante esta petición.
         */
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        authorities.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    /**
     * Comprueba si la petición trae un JWT
     * en el formato correcto.
     */
    private boolean tieneJWT(HttpServletRequest request) {

        String authorizationHeader =
                request.getHeader(HEADER_AUTHORIZATION);

        return authorizationHeader != null
                && authorizationHeader.startsWith(TOKEN_BEARER_PREFIX);
    }

    /**
     * Método que se ejecuta automáticamente
     * por cada petición que pasa por el Gateway.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {

            /*
             * Si viene un JWT, intentamos validarlo.
             */
            if (tieneJWT(request)) {

                Claims claims = obtenerClaims(request);

                /*
                 * Si el token contiene permisos,
                 * autenticamos al usuario.
                 */
                if (claims.get("authorities") != null) {

                    establecerAutenticacion(claims);

                } else {

                    SecurityContextHolder.clearContext();
                }

            } else {

                /*
                 * Si no viene token,
                 * no existe usuario autenticado.
                 */
                SecurityContextHolder.clearContext();
            }

            /*
             * Permitimos que Spring continúe
             * procesando la petición.
             *
             * Más adelante WebSecurityConfig decidirá
             * si esa ruta puede continuar o no.
             */
            filterChain.doFilter(request, response);

        } catch (
                ExpiredJwtException |
                UnsupportedJwtException |
                MalformedJwtException e) {

            /*
             * Si el token está vencido o tiene un formato
             * incorrecto, detenemos la petición.
             */
            SecurityContextHolder.clearContext();

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Token JWT invalido o expirado."
            );
        }
    }
}