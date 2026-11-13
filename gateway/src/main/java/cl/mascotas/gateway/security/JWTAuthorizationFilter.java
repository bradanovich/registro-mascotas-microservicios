package cl.mascotas.gateway.security;

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

import static cl.mascotas.gateway.security.Constants.*;

/**
 * Filtro encargado de validar el JWT
 * antes de permitir que una peticion atraviese el API Gateway.
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Obtiene el JWT desde el header Authorization
     * y valida su firma.
     */
    private Claims obtenerClaims(HttpServletRequest request) {

        /*
         * Ejemplo recibido:
         *
         * Authorization: Bearer eyJ...
         */
        String authorizationHeader =
                request.getHeader(HEADER_AUTHORIZATION);

        /*
         * Eliminamos "Bearer "
         * para quedarnos solamente con el JWT.
         */
        String jwtToken =
                authorizationHeader.replace(TOKEN_BEARER_PREFIX, "");

        /*
         * Validamos:
         * - Firma
         * - Integridad
         * - Fecha de expiracion
         *
         * Utilizamos la misma clave secreta
         * que Usuarios MS utiliza para crear el token.
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
         * Recuperamos del JWT el claim:
         *
         * authorities
         *
         * En nuestro proyecto contiene:
         * ROLE_USER
         */
        Object authoritiesClaim =
                claims.get("authorities");

        /*
         * Verificamos de forma segura que realmente
         * sea una lista.
         *
         * Esto evita el warning que aparecia antes
         * al convertir directamente a List<String>.
         */
        if (!(authoritiesClaim instanceof List<?> listaAuthorities)) {

            SecurityContextHolder.clearContext();
            return;
        }

        /*
         * Transformamos cada permiso recibido
         * en una autoridad reconocida por Spring Security.
         */
        List<SimpleGrantedAuthority> authorities =
                listaAuthorities.stream()
                        .map(Object::toString)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        /*
         * El subject del JWT contiene el email
         * del usuario autenticado.
         */
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        authorities
                );

        /*
         * Guardamos la autenticacion durante
         * esta peticion.
         */
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    /**
     * Comprueba si la peticion contiene
     * un JWT en el formato Bearer.
     */
    private boolean tieneJWT(HttpServletRequest request) {

        String authorizationHeader =
                request.getHeader(HEADER_AUTHORIZATION);

        return authorizationHeader != null
                && authorizationHeader.startsWith(TOKEN_BEARER_PREFIX);
    }

    /**
     * Este metodo se ejecuta una vez
     * por cada peticion que pasa por el Gateway.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {

            /*
             * Si la peticion trae JWT,
             * intentamos validarlo.
             */
            if (tieneJWT(request)) {

                Claims claims =
                        obtenerClaims(request);

                /*
                 * Si contiene permisos,
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
                 * si la ruta es publica o protegida.
                 */
                SecurityContextHolder.clearContext();
            }

            /*
             * Continuamos con la peticion.
             */
            filterChain.doFilter(
                    request,
                    response
            );

        } catch (JwtException | IllegalArgumentException e) {

            /*
             * Entramos aqui cuando el token:
             *
             * - Esta vencido
             * - Tiene firma incorrecta
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