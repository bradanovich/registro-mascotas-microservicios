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

import cl.mascotas.salud.model.Desparasitacion;
import cl.mascotas.salud.service.DesparasitacionesService;

@RestController
@RequestMapping("/mascotas-app/salud/desparasitaciones")
public class DesparasitacionesController {
    @Autowired
    private DesparasitacionesService ds;

    @GetMapping
    @Operation(summary = "Listar desparasitaciones", description = "Lista todas las desparasitaciones registradas")
    public List<Desparasitacion> listarDesparasitaciones() {
        return ds.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar desparasitacion por id", description = "Busca una desparasitacion por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Desparasitacion encontrada"),
        @ApiResponse(responseCode = "404", description = "Desparasitacion no encontrada")
    })
    public ResponseEntity<Desparasitacion> obtener(@PathVariable Integer id) {
        try {
            Desparasitacion d = ds.buscarPorId(id);
            return ResponseEntity.ok(d);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Registrar desparasitacion", description = "Registra una desparasitacion (valida que la mascota exista)")
    public ResponseEntity<?> crear(@RequestBody Desparasitacion desparasitacion) {
        try {
            Desparasitacion d = ds.guardarDesparasitacion(desparasitacion);
            return ResponseEntity.status(HttpStatus.CREATED).body("201 \n La desparasitacion se ha registrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar desparasitacion", description = "Actualiza los datos de una desparasitacion")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Desparasitacion desparasitacion) {
        try {
            Desparasitacion d = ds.actualizarDesparasitacion(id, desparasitacion);
            return ResponseEntity.ok(d);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar desparasitacion", description = "Elimina una desparasitacion por su identificador")
    public ResponseEntity<?> eliminarDesparasitacion(@PathVariable Integer id) {
        try {
            boolean d = ds.eliminarDesparasitacion(id);
            if (d) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("204 \n Desparasitacion eliminada correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
        }
    }
}
