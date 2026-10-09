package cl.mascotas.gateway.security;

import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * Constantes utilizadas para validar los tokens JWT
 * que llegan al API Gateway.
 */
public class Constants {

    /**
     * Header HTTP donde viaja el token.
     *
     * Ejemplo:
     * Authorization: Bearer eyJ...
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * Prefijo estándar del token JWT.
     */
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    /**
     * Debe ser exactamente la misma clave utilizada
     * por el microservicio Usuarios para firmar el JWT.
     */
    public static final String SUPER_SECRET_KEY =
            "registroMascotasClaveSecretaJWT2026DuocUCProyectoMicroserviciosSeguros";

    /**
     * Convierte la clave secreta de texto
     * en una Key compatible con JJWT.
     */
    public static Key getSigningKey(String secret) {

        byte[] keyBytes =
                secret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}