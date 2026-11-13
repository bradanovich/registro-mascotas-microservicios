package cl.mascotas.salud.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad DESPARASITACION.
 * PK: idDesparasitacion
 * FK: idMascota -> MASCOTA.idMascota (otro microservicio, se resuelve via Feign)
 */
@Entity
@Table(name = "DESPARASITACION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Este objeto representa una desparasitacion aplicada a una mascota.")
public class Desparasitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDesparasitacion", nullable = false, unique = true)
    @Schema(description = "Identificador unico de la desparasitacion", example = "1")
    private Integer idDesparasitacion;

    @Column(name = "idMascota", nullable = false)
    @Schema(description = "Identificador de la mascota", example = "1")
    private Integer idMascota;

    @Column(name = "tipo", length = 20, nullable = false)
    @Schema(description = "Tipo de desparasitacion", example = "Interna")
    private String tipo;

    @Column(name = "producto", length = 50)
    @Schema(description = "Producto utilizado", example = "Drontal")
    private String producto;

    @Column(name = "fecha", nullable = false)
    @Schema(description = "Fecha de aplicacion", example = "2026-03-20")
    private LocalDate fecha;
}
