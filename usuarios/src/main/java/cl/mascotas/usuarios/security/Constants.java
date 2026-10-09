package cl.mascotas.usuarios.security;

import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * Clase que contiene las constantes utilizadas por JWT.
 *
 * Aqui definimos:
 * - El nombre del header donde viaja el token.
 * - El prefijo Bearer.
 * - La clave secreta utilizada para firmar el JWT.
 * - El tiempo de duracion del token.
 */
public class Constants {

    /*
     * Header HTTP donde se enviara el JWT.
     *
     * Ejemplo:
     * Authorization: Bearer eyJhbGciOi...
     */
    public static final String HEADER_AUTHORIZATION =
            "Authorization";

    /*
     * Prefijo que se coloca antes del token.
     */
    public static final String TOKEN_BEARER_PREFIX =
            "Bearer ";

    /*
     * Clave secreta utilizada para firmar y validar el JWT.
     *
     * Para este proyecto academico la dejamos aqui para
     * seguir una estructura similar al ejemplo del profesor.
     *
     * Mas adelante, cuando dockericemos, podemos explicar
     * que idealmente esta clave se entrega mediante una
     * variable de entorno.
     */
    public static final String SUPER_SECRET_KEY =
            "registroMascotasClaveSecretaJWT2026DuocUCProyectoMicroserviciosSeguros";

    /*
     * Duracion del token.
     *
     * 24 horas expresadas en milisegundos:
     *
     * 1000 ms
     * x 60 segundos
     * x 60 minutos
     * x 24 horas
     */
    public static final long TOKEN_EXPIRATION_TIME =
            1000L * 60 * 60 * 24;

    /**
     * Convierte nuestra clave secreta String en una Key
     * que JJWT puede utilizar para firmar y validar tokens.
     */
    public static Key getSigningKey(String secret) {

        byte[] keyBytes =
                secret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}