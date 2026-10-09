package cl.mascotas.usuarios.security;

import io.jsonwebtoken.Jwts;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cl.mascotas.usuarios.security.Constants.*;

/**
 * Clase encargada de GENERAR el token JWT.
 *
 * El token se crea cuando un usuario inicia sesion correctamente.
 */
@Configuration
public class JWTAuthenticationConfig {

    /**
     * Genera un JWT para el usuario autenticado.
     *
     * @param email email del usuario que inicio sesion
     * @return token JWT con prefijo "Bearer "
     */
    public String getJWTToken(String email) {

        /*
         * Por ahora todos los usuarios tendran el rol ROLE_USER.
         *
         * Este rol queda almacenado dentro del JWT.
         */
        List<GrantedAuthority> grantedAuthorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        "ROLE_USER"
                );

        /*
         * Los claims son datos adicionales que podemos
         * guardar dentro del token.
         *
         * En este caso guardamos los permisos del usuario.
         */
        Map<String, Object> claims = new HashMap<>();

        claims.put(
                "authorities",
                grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        /*
         * Construimos el JWT.
         */
        String token = Jwts.builder()

                // Agregamos los permisos
                .claims()
                .add(claims)

                // Guardamos el email como identificador del usuario
                .subject(email)

                // Fecha en la que se creo el token
                .issuedAt(
                        new Date(System.currentTimeMillis())
                )

                // Fecha de vencimiento del token
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + TOKEN_EXPIRATION_TIME
                        )
                )

                .and()

                // Firmamos el token con nuestra clave secreta
                .signWith(
                        getSigningKey(SUPER_SECRET_KEY)
                )

                // Finalmente generamos el String JWT
                .compact();

        /*
         * Devolvemos el token con el prefijo Bearer,
         * igual que en el ejemplo del profesor.
         */
        return TOKEN_BEARER_PREFIX + token;
    }
}