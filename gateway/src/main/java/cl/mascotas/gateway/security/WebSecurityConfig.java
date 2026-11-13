package cl.mascotas.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuracion principal de seguridad del API Gateway.
 *
 * Aqui decidimos:
 * - Que rutas son publicas.
 * - Que rutas necesitan JWT.
 * - Que filtro valida el token.
 */
@Configuration
public class WebSecurityConfig {

    /*
     * Nuestro filtro JWT.
     *
     * Spring lo entrega automaticamente porque
     * JWTAuthorizationFilter tiene @Component.
     */
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    public WebSecurityConfig(
            JWTAuthorizationFilter jwtAuthorizationFilter) {

        this.jwtAuthorizationFilter =
                jwtAuthorizationFilter;
    }

    /**
     * Configura las reglas de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                /*
                 * Desactivamos CSRF porque nuestra API REST
                 * no trabaja con formularios ni sesiones web.
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * JWT es STATELESS.
                 *
                 * Esto significa que el servidor
                 * no guarda sesiones de usuarios.
                 *
                 * Cada peticion debe demostrar
                 * su identidad mediante el token.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                /*
                 * Definimos que rutas pueden entrar
                 * sin autenticacion.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * REGISTRO PUBLICO
                         *
                         * Un usuario nuevo todavía no tiene
                         * token, por eso esta ruta debe ser publica.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/mascotas-app/usuarios/registro"
                        ).permitAll()

                        /*
                         * LOGIN PUBLICO
                         *
                         * El usuario necesita entrar aqui
                         * precisamente para obtener su JWT.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/mascotas-app/usuarios/login"
                        ).permitAll()

                        /*
                         * Cualquier otra ruta que atraviese
                         * el Gateway requiere autenticacion.
                         *
                         * Ejemplos:
                         *
                         * GET  /usuarios/3
                         * POST /mascotas
                         * POST /salud/fichas
                         * POST /citas
                         * POST /recordatorios
                         */
                        .anyRequest()
                        .authenticated()
                )

                /*
                 * Nuestro filtro JWT debe ejecutarse
                 * antes del filtro normal de autenticacion
                 * de Spring Security.
                 */
                .addFilterBefore(
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}