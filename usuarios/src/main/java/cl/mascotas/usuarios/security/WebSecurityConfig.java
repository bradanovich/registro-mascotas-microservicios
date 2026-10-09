package cl.mascotas.usuarios.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuracion principal de Spring Security.
 *
 * Aqui definimos:
 *
 * - Que endpoints son publicos.
 * - Que endpoints necesitan autenticacion.
 * - Que filtro revisara el JWT.
 */
@Configuration
public class WebSecurityConfig {

    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    /**
     * Inyeccion de dependencias por constructor.
     *
     * Spring nos entrega automaticamente
     * JWTAuthorizationFilter porque tiene @Component.
     */
    public WebSecurityConfig(
            JWTAuthorizationFilter jwtAuthorizationFilter) {

        this.jwtAuthorizationFilter =
                jwtAuthorizationFilter;
    }

    /**
     * Configura las reglas de seguridad
     * del microservicio Usuarios.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                /*
                 * Desactivamos CSRF porque trabajamos
                 * con una API REST y autenticacion JWT.
                 */
                .csrf(csrf ->
                        csrf.disable()
                )

                /*
                 * JWT no necesita mantener una sesion
                 * almacenada en el servidor.
                 *
                 * Cada peticion debe enviar su propio token.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                /*
                 * Reglas de acceso a los endpoints.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * REGISTRO PUBLICO
                         *
                         * Un usuario nuevo aun no tiene token,
                         * por eso debe poder registrarse.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/mascotas-app/usuarios/registro"
                        )
                        .permitAll()

                        /*
                         * LOGIN PUBLICO
                         *
                         * El usuario entra aqui con email
                         * y contrasena para obtener el JWT.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/mascotas-app/usuarios/login"
                        )
                        .permitAll()

                        /*
                         * Este endpoint lo utiliza Mascotas MS
                         * mediante OpenFeign para validar
                         * que el dueno exista.
                         *
                         * Lo dejamos disponible para la
                         * comunicacion interna entre servicios.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/mascotas-app/usuarios/dto/**"
                        )
                        .permitAll()

                        /*
                         * Swagger queda disponible para
                         * documentar y probar la API.
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        )
                        .permitAll()

                        /*
                         * Cualquier otra peticion necesita
                         * un JWT valido.
                         */
                        .anyRequest()
                        .authenticated()
                )

                /*
                 * Agregamos nuestro filtro JWT.
                 *
                 * Este filtro revisara:
                 *
                 * Authorization: Bearer TOKEN
                 */
                .addFilterBefore(
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}