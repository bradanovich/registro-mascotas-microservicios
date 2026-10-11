package cl.mascotas.citas.controller;

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

import cl.mascotas.citas.model.Cita;
import cl.mascotas.citas.service.CitasService;

@RestController
@RequestMapping("/mascotas-app/citas")
public class CitasController {
    @Autowired
    private CitasService cs;

    @GetMapping
    @Operation(summary = "Listar citas", description = "Lista todas las citas registradas")
    public List<Cita> listarCitas() {
        return cs.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cita por id", description = "Busca una cita por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita encontrada"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<Cita> obtener(@PathVariable Integer id) {
        try {
            Cita c = cs.buscarPorId(id);
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear cita", description = "Crea una nueva cita veterinaria (valida que la mascota exista)")
    public ResponseEntity<?> crear(@RequestBody Cita cita) {
        try {
            Cita c = cs.guardarCita(cita);
            return ResponseEntity.status(HttpStatus.CREATED).body("201 \n La cita se ha creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cita", description = "Actualiza los datos de una cita")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Cita cita) {
        try {
            Cita c = cs.actualizarCita(id, cita);
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cita", description = "Elimina una cita por su identificador")
    public ResponseEntity<?> eliminarCita(@PathVariable Integer id) {
        try {
            boolean c = cs.eliminarCita(id);
            if (c) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("204 \n Cita eliminada correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
        }
    }
}
