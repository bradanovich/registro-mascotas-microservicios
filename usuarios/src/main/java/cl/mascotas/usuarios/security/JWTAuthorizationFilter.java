package cl.mascotas.usuarios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

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

import static cl.mascotas.usuarios.security.Constants.*;

/**
 * Filtro encargado de validar el JWT
 * que llega en cada peticion al microservicio Usuarios.
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Extrae y valida el JWT enviado en el header Authorization.
     */
    private Claims obtenerClaims(HttpServletRequest request) {

        // Ejemplo:
        // Authorization: Bearer eyJ...
        String authorizationHeader =
                request.getHeader(HEADER_AUTHORIZATION);

        // Quitamos el texto "Bearer "
        // y dejamos solamente el token.
        String jwtToken =
                authorizationHeader.replace(TOKEN_BEARER_PREFIX, "");

        /*
         * Validamos:
         * - Firma del token
         * - Integridad
         * - Fecha de expiracion
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
     * Registra al usuario como autenticado
     * dentro de Spring Security.
     */
    private void establecerAutenticacion(Claims claims) {

        /*
         * Recuperamos el claim "authorities".
         *
         * Antes haciamos:
         *
         * (List<String>) claims.get("authorities")
         *
         * Eso funcionaba, pero Java mostraba el warning
         * "unchecked or unsafe operations".
         *
         * Ahora usamos List<?> y convertimos sus valores
         * de forma segura.
         */
        Object authoritiesClaim =
                claims.get("authorities");

        if (!(authoritiesClaim instanceof List<?> listaAuthorities)) {
            SecurityContextHolder.clearContext();
            return;
        }

        /*
         * Convertimos cada permiso recibido
         * en una autoridad que Spring Security entiende.
         *
         * Ejemplo:
         * ROLE_USER
         */
        List<SimpleGrantedAuthority> authorities =
                listaAuthorities.stream()
                        .map(Object::toString)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        /*
         * El subject del token contiene el email
         * del usuario que inicio sesion.
         */
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        authorities
                );

        // Guardamos al usuario como autenticado.
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    /**
     * Comprueba si la peticion trae un JWT.
     */
    private boolean tieneJWT(HttpServletRequest request) {

        String authorizationHeader =
                request.getHeader(HEADER_AUTHORIZATION);

        return authorizationHeader != null
                && authorizationHeader.startsWith(TOKEN_BEARER_PREFIX);
    }

    /**
     * Este metodo se ejecuta automaticamente
     * una vez por cada peticion.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {

            /*
             * Si viene un JWT intentamos validarlo.
             */
            if (tieneJWT(request)) {

                Claims claims =
                        obtenerClaims(request);

                /*
                 * Si encontramos authorities,
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
                 * dejamos la peticion sin autenticacion.
                 *
                 * WebSecurityConfig decide despues
                 * si esa ruta es publica o protegida.
                 */
                SecurityContextHolder.clearContext();
            }

            filterChain.doFilter(
                    request,
                    response
            );

        } catch (JwtException | IllegalArgumentException e) {

            /*
             * Aqui entramos si el JWT:
             * - Esta vencido
             * - Tiene una firma incorrecta
             * - Esta mal formado
             * - No puede ser procesado
             */
            SecurityContextHolder.clearContext();

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Token JWT invalido o expirado."
            );
        }
    }
}