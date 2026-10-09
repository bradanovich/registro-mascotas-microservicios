package cl.mascotas.usuarios.security;

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

import static cl.mascotas.usuarios.security.Constants.*;

/**
 * Filtro encargado de VALIDAR el JWT.
 *
 * Esta clase revisa cada peticion que llega al microservicio
 * Usuarios y busca el token en el header Authorization.
 *
 * Ejemplo:
 *
 * Authorization: Bearer eyJhbGciOi...
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Extrae el token desde el header Authorization
     * y valida su firma utilizando nuestra clave secreta.
     *
     * Si el token es valido, devuelve la informacion
     * almacenada dentro del JWT.
     */
    private Claims obtenerClaims(HttpServletRequest request) {

        /*
         * Obtenemos:
         *
         * Bearer eyJhbGciOi...
         */
        String authorizationHeader =
                request.getHeader(HEADER_AUTHORIZATION);

        /*
         * Quitamos "Bearer "
         * para quedarnos solamente con el JWT.
         */
        String jwtToken =
                authorizationHeader.replace(
                        TOKEN_BEARER_PREFIX,
                        ""
                );

        /*
         * JJWT valida la firma del token utilizando
         * la misma clave con la que fue creado.
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
         * Recuperamos los roles guardados dentro del token.
         *
         * En nuestro caso:
         * ROLE_USER
         */
        List<String> authorities =
                (List<String>) claims.get("authorities");

        /*
         * Creamos el objeto que representa al usuario
         * autenticado dentro de Spring Security.
         *
         * claims.getSubject() contiene el email
         * que guardamos al crear el JWT.
         */
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        authorities.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                );

        /*
         * Guardamos la autenticacion en el contexto
         * de seguridad de Spring.
         */
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    /**
     * Comprueba si la peticion contiene un JWT.
     */
    private boolean tieneJWT(HttpServletRequest request) {

        String authorizationHeader =
                request.getHeader(HEADER_AUTHORIZATION);

        /*
         * El header debe existir y comenzar con:
         *
         * Bearer
         */
        return authorizationHeader != null
                && authorizationHeader.startsWith(
                TOKEN_BEARER_PREFIX
        );
    }

    /**
     * Este metodo se ejecuta una vez por cada peticion HTTP.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {

            /*
             * Si existe un JWT, intentamos validarlo.
             */
            if (tieneJWT(request)) {

                Claims claims =
                        obtenerClaims(request);

                /*
                 * Si el token contiene roles,
                 * autenticamos al usuario.
                 */
                if (claims.get("authorities") != null) {

                    establecerAutenticacion(claims);

                } else {

                    SecurityContextHolder.clearContext();
                }

            } else {

                /*
                 * Si no hay JWT, dejamos al usuario
                 * como no autenticado.
                 *
                 * Luego WebSecurityConfig decidira
                 * si puede acceder o no al endpoint.
                 */
                SecurityContextHolder.clearContext();
            }

            /*
             * La peticion continua hacia el controlador.
             */
            filterChain.doFilter(
                    request,
                    response
            );

        } catch (
                ExpiredJwtException
                | UnsupportedJwtException
                | MalformedJwtException e) {

            /*
             * Si el JWT esta vencido, mal formado
             * o no es compatible, rechazamos la peticion.
             */
            SecurityContextHolder.clearContext();

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Token JWT invalido o expirado."
            );
        }
    }
}