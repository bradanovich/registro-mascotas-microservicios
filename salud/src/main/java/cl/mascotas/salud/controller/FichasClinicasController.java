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

import cl.mascotas.salud.model.FichaClinica;
import cl.mascotas.salud.service.FichasClinicasService;

@RestController
@RequestMapping("/mascotas-app/salud/fichas-clinicas")
public class FichasClinicasController {
    @Autowired
    private FichasClinicasService fs;

    @GetMapping
    @Operation(summary = "Listar fichas clinicas", description = "Lista todas las fichas clinicas registradas")
    public List<FichaClinica> listarFichasClinicas() {
        return fs.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ficha clinica por id", description = "Busca una ficha clinica por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ficha clinica encontrada"),
        @ApiResponse(responseCode = "404", description = "Ficha clinica no encontrada")
    })
    public ResponseEntity<FichaClinica> obtener(@PathVariable Integer id) {
        try {
            FichaClinica f = fs.buscarPorId(id);
            return ResponseEntity.ok(f);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Registrar ficha clinica", description = "Registra una ficha clinica (valida que la mascota exista)")
    public ResponseEntity<?> crear(@RequestBody FichaClinica ficha) {
        try {
            FichaClinica f = fs.guardarFichaClinica(ficha);
            return ResponseEntity.status(HttpStatus.CREATED).body("201 \n La ficha clinica se ha registrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ficha clinica", description = "Actualiza los datos de una ficha clinica")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody FichaClinica ficha) {
        try {
            FichaClinica f = fs.actualizarFichaClinica(id, ficha);
            return ResponseEntity.ok(f);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ficha clinica", description = "Elimina una ficha clinica por su identificador")
    public ResponseEntity<?> eliminarFichaClinica(@PathVariable Integer id) {
        try {
            boolean f = fs.eliminarFichaClinica(id);
            if (f) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("204 \n Ficha clinica eliminada correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR 400 \n La solicitud no ha podido ser procesada.");
        }
    }
}
