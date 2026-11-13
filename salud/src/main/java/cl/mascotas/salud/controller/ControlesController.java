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

import cl.mascotas.salud.model.Control;
import cl.mascotas.salud.service.ControlesService;

@RestController
@RequestMapping("/mascotas-app/salud/controles")
public class ControlesController {
    @Autowired
    private ControlesService cs;

    @GetMapping
    @Operation(summary = "Listar controles", description = "Lista todos los controles registrados")
    public List<Control> listarControles() {
        return cs.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar control por id", description = "Busca un control por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Control encontrado"),
        @ApiResponse(responseCode = "404", description = "Control no encontrado")
    })
    public ResponseEntity<Control> obtener(@PathVariable Integer id) {
        try {
            Control c = cs.buscarPorId(id);
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Registrar control", description = "Registra un control veterinario (valida que la mascota exista)")
    public ResponseEntity<?> crear(@RequestBody Control control) {
        try {
            Control c = cs.guardarControl(control);
            return ResponseEntity.status(HttpStatus.CREATED).body("201 \n El control se ha registrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar control", description = "Actualiza los datos de un control")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Control control) {
        try {
            Control c = cs.actualizarControl(id, control);
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar control", description = "Elimina un control por su identificador")
    public ResponseEntity<?> eliminarControl(@PathVariable Integer id) {
        try {
            boolean c = cs.eliminarControl(id);
            if (c) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("204 \n Control eliminado correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
        }
    }
}
