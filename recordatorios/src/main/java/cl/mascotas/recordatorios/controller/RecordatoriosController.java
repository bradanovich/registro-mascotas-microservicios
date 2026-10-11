package cl.mascotas.recordatorios.controller;

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

import cl.mascotas.recordatorios.model.Recordatorio;
import cl.mascotas.recordatorios.service.RecordatoriosService;

@RestController
@RequestMapping("/mascotas-app/recordatorios")
public class RecordatoriosController {
    @Autowired
    private RecordatoriosService rs;

    @GetMapping
    @Operation(summary = "Listar recordatorios", description = "Lista todos los recordatorios registrados")
    public List<Recordatorio> listarRecordatorios() {
        return rs.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar recordatorio por id", description = "Busca un recordatorio por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recordatorio encontrado"),
        @ApiResponse(responseCode = "404", description = "Recordatorio no encontrado")
    })
    public ResponseEntity<Recordatorio> obtener(@PathVariable Integer id) {
        try {
            Recordatorio r = rs.buscarPorId(id);
            return ResponseEntity.ok(r);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear recordatorio", description = "Crea un nuevo recordatorio (valida que la mascota exista)")
    public ResponseEntity<?> crear(@RequestBody Recordatorio recordatorio) {
        try {
            Recordatorio r = rs.guardarRecordatorio(recordatorio);
            return ResponseEntity.status(HttpStatus.CREATED).body("201 \n El recordatorio se ha creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar recordatorio", description = "Actualiza los datos de un recordatorio")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Recordatorio recordatorio) {
        try {
            Recordatorio r = rs.actualizarRecordatorio(id, recordatorio);
            return ResponseEntity.ok(r);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar recordatorio", description = "Elimina un recordatorio por su identificador")
    public ResponseEntity<?> eliminarRecordatorio(@PathVariable Integer id) {
        try {
            boolean r = rs.eliminarRecordatorio(id);
            if (r) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("204 \n Recordatorio eliminado correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
        }
    }
}
