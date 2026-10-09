package cl.mascotas.usuarios.controller;

import java.util.List;
import java.util.Map;

import cl.mascotas.usuarios.dto.UsuarioDTO;
import cl.mascotas.usuarios.model.Usuario;
import cl.mascotas.usuarios.security.JWTAuthenticationConfig;
import cl.mascotas.usuarios.service.UsuariosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mascotas-app/usuarios")
public class UsuariosController {

    private final UsuariosService us;

    /*
     * Esta clase es la encargada de generar
     * el token JWT después de un login correcto.
     */
    private final JWTAuthenticationConfig jwtAuthenticationConfig;

    /*
     * Inyección de dependencias por constructor.
     *
     * Spring nos entrega:
     * - UsuariosService para trabajar con usuarios.
     * - JWTAuthenticationConfig para generar tokens.
     */
    public UsuariosController(
            UsuariosService us,
            JWTAuthenticationConfig jwtAuthenticationConfig) {

        this.us = us;
        this.jwtAuthenticationConfig = jwtAuthenticationConfig;
    }

    // ============================================================
    // LISTAR TODOS LOS USUARIOS
    // ============================================================

    @GetMapping
    @Operation(
            summary = "Listar usuarios",
            description = "Lista todos los usuarios registrados"
    )
    public ResponseEntity<List<Usuario>> listarUsuarios() {

        return ResponseEntity.ok(us.listar());
    }

    // ============================================================
    // BUSCAR USUARIO POR ID
    // ============================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuario por id",
            description = "Busca un usuario por su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public ResponseEntity<?> obtener(
            @PathVariable Integer id) {

        try {

            Usuario usuario =
                    us.buscarPorId(id);

            return ResponseEntity.ok(usuario);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // ============================================================
    // BUSCAR USUARIO UTILIZANDO DTO
    // ============================================================

    @GetMapping("/dto/{id}")
    @Operation(
            summary = "Buscar usuario DTO por id",
            description = "Busca un usuario y devuelve sus datos mediante un DTO"
    )
    public ResponseEntity<?> buscarUsuarioDTO(
            @PathVariable Integer id) {

        try {

            UsuarioDTO dto =
                    us.buscarUsuarioDTO(id);

            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // ============================================================
    // REGISTRAR USUARIO
    // ============================================================

    @PostMapping("/registro")
    @Operation(
            summary = "Registrar usuario",
            description = "Registra un nuevo usuario en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos incorrectos o email ya registrado"
            )
    })
    public ResponseEntity<?> registrar(
            @Valid @RequestBody Usuario usuario) {

        try {

            Usuario nuevoUsuario =
                    us.registrar(usuario);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevoUsuario);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ============================================================
    // LOGIN + GENERACIÓN DE JWT
    // ============================================================

    @PostMapping("/login")
    @Operation(
            summary = "Login de usuario",
            description = "Valida email y contrasena y genera un token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login correcto y token JWT generado"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales incorrectas"
            )
    })
    public ResponseEntity<?> login(
            @RequestBody Usuario usuario) {

        try {

            /*
             * PRIMER PASO:
             *
             * Validamos el email y contraseña utilizando
             * el servicio que ya teníamos funcionando.
             */
            Usuario usuarioEncontrado =
                    us.login(
                            usuario.getEmail(),
                            usuario.getPassword()
                    );

            /*
             * SEGUNDO PASO:
             *
             * Si llegamos hasta aquí significa que las
             * credenciales eran correctas.
             *
             * Generamos un JWT utilizando el email
             * del usuario como identificador.
             */
            String token =
                    jwtAuthenticationConfig
                            .getJWTToken(
                                    usuarioEncontrado.getEmail()
                            );

            /*
             * TERCER PASO:
             *
             * Devolvemos un JSON con:
             *
             * - mensaje
             * - token JWT
             * - información del usuario
             *
             * La contraseña NO aparecerá porque en Usuario
             * tenemos @JsonProperty(WRITE_ONLY).
             */
            Map<String, Object> respuesta =
                    Map.of(
                            "mensaje", "Login correcto",
                            "token", token,
                            "usuario", usuarioEncontrado
                    );

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {

            /*
             * Si el email no existe o la contraseña
             * es incorrecta respondemos 401.
             */
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    // ============================================================
    // ACTUALIZAR USUARIO
    // ============================================================

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar perfil",
            description = "Actualiza los datos de un usuario"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos incorrectos"
            )
    })
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Usuario usuario) {

        try {

            Usuario usuarioActualizado =
                    us.actualizar(id, usuario);

            return ResponseEntity.ok(usuarioActualizado);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ============================================================
    // ELIMINAR USUARIO
    // ============================================================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario por su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public ResponseEntity<?> eliminarUsuario(
            @PathVariable Integer id) {

        try {

            us.eliminar(id);

            return ResponseEntity
                    .noContent()
                    .build();

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}