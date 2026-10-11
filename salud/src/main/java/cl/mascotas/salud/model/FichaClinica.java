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
 * Entidad FICHA_CLINICA.
 * PK: idFicha
 * FK: idMascota -> MASCOTA.idMascota (otro microservicio, se resuelve via Feign)
 */
@Entity
@Table(name = "FICHA_CLINICA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Este objeto representa la ficha clinica de una mascota.")
public class FichaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFicha", nullable = false, unique = true)
    @Schema(description = "Identificador unico de la ficha", example = "1")
    private Integer idFicha;

    @Column(name = "idMascota", nullable = false)
    @Schema(description = "Identificador de la mascota", example = "1")
    private Integer idMascota;

    @Column(name = "fecha", nullable = false)
    @Schema(description = "Fecha de la atencion", example = "2026-04-01")
    private LocalDate fecha;

    @Column(name = "diagnostico", length = 255, nullable = false)
    @Schema(description = "Diagnostico clinico", example = "Otitis leve")
    private String diagnostico;

    @Column(name = "tratamiento", length = 255)
    @Schema(description = "Tratamiento indicado", example = "Gotas otologicas 7 dias")
    private String tratamiento;
}
