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
 * Entidad VACUNA.
 * PK: idVacuna
 * FK: idMascota -> MASCOTA.idMascota (otro microservicio, se resuelve via Feign)
 */
@Entity
@Table(name = "VACUNA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Este objeto representa una vacuna aplicada a una mascota.")
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVacuna", nullable = false, unique = true)
    @Schema(description = "Identificador unico de la vacuna", example = "1")
    private Integer idVacuna;

    @Column(name = "idMascota", nullable = false)
    @Schema(description = "Identificador de la mascota vacunada", example = "1")
    private Integer idMascota;

    @Column(name = "nombreVacuna", length = 50, nullable = false)
    @Schema(description = "Nombre de la vacuna", example = "Antirrabica")
    private String nombreVacuna;

    @Column(name = "fecha", nullable = false)
    @Schema(description = "Fecha de aplicacion", example = "2026-03-15")
    private LocalDate fecha;

    @Column(name = "dosis", length = 20)
    @Schema(description = "Dosis aplicada", example = "1ra")
    private String dosis;
}
