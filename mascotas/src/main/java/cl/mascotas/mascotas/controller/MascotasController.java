package cl.mascotas.mascotas.controller;

import java.util.List;

import cl.mascotas.mascotas.dto.MascotaDTO;
import cl.mascotas.mascotas.model.Mascota;
import cl.mascotas.mascotas.service.MascotasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mascotas-app/mascotas")
public class MascotasController {

    private final MascotasService ms;

    // Inyeccion de dependencias por constructor
    public MascotasController(MascotasService ms) {
        this.ms = ms;
    }

    // LISTAR TODAS LAS MASCOTAS
    @GetMapping
    @Operation(
            summary = "Listar mascotas",
            description = "Lista todas las mascotas registradas"
    )
    public ResponseEntity<List<Mascota>> listarMascotas() {

        return ResponseEntity.ok(ms.listar());
    }

    // BUSCAR MASCOTA POR ID
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar mascota por id",
            description = "Busca una mascota por su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mascota encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mascota no encontrada"
            )
    })
    public ResponseEntity<?> obtener(
            @PathVariable Integer id) {

        try {

            Mascota mascota = ms.buscarPorId(id);

            return ResponseEntity.ok(mascota);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // BUSCAR MASCOTA MEDIANTE DTO
    @GetMapping("/dto/{id}")
    @Operation(
            summary = "Buscar mascota DTO por id",
            description = "Busca una mascota y devuelve una version resumida mediante DTO"
    )
    public ResponseEntity<?> buscarMascotaDTO(
            @PathVariable Integer id) {

        try {

            MascotaDTO dto = ms.buscarMascotaDTO(id);

            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // CREAR MASCOTA
    @PostMapping
    @Operation(
            summary = "Crear mascota",
            description = "Crea una nueva mascota y valida que el dueno exista en Usuarios MS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Mascota creada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos incorrectos o dueno inexistente"
            )
    })
    public ResponseEntity<?> crear(
            @Valid @RequestBody Mascota mascota) {

        try {

            Mascota nuevaMascota =
                    ms.guardarMascota(mascota);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevaMascota);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ACTUALIZAR MASCOTA
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar mascota",
            description = "Actualiza los datos de una mascota y valida que el dueno exista"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mascota actualizada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos incorrectos o dueno inexistente"
            )
    })
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Mascota mascota) {

        try {

            Mascota mascotaActualizada =
                    ms.actualizarMascota(id, mascota);

            return ResponseEntity.ok(mascotaActualizada);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ELIMINAR MASCOTA
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar mascota",
            description = "Elimina una mascota por su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Mascota eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mascota no encontrada"
            )
    })
    public ResponseEntity<?> eliminarMascota(
            @PathVariable Integer id) {

        try {

            ms.eliminarMascota(id);

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