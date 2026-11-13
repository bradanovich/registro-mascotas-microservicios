package cl.mascotas.salud.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mascotas.salud.model.Vacuna;
import cl.mascotas.salud.service.VacunasService;

@RestController
@RequestMapping("/mascotas-app/salud/vacunas")
public class VacunasController {
    @Autowired
    private VacunasService vs;

    @GetMapping
    @Operation(summary = "Listar vacunas", description = "Lista todas las vacunas registradas")
    public List<Vacuna> listarVacunas() {
        return vs.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vacuna por id", description = "Busca una vacuna por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vacuna encontrada"),
        @ApiResponse(responseCode = "404", description = "Vacuna no encontrada")
    })
    public ResponseEntity<Vacuna> obtener(@PathVariable Integer id) {
        try {
            Vacuna v = vs.buscarPorId(id);
            return ResponseEntity.ok(v);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Registrar vacuna", description = "Registra una vacuna (valida que la mascota exista antes de guardar)")
    public ResponseEntity<?> crear(@RequestBody Vacuna vacuna) {
        try {
            Vacuna v = vs.guardarVacuna(vacuna);
            return ResponseEntity.status(HttpStatus.CREATED).body("201 \n La vacuna se ha registrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vacuna", description = "Actualiza los datos de una vacuna")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Vacuna vacuna) {
        try {
            Vacuna v = vs.actualizarVacuna(id, vacuna);
            return ResponseEntity.ok(v);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vacuna", description = "Elimina una vacuna por su identificador")
    public ResponseEntity<?> eliminarVacuna(@PathVariable Integer id) {
        try {
            boolean v = vs.eliminarVacuna(id);
            if (v) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("204 \n Vacuna eliminada correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
        }
    }
}
