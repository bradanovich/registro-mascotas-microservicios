package cl.mascotas.citas.model;

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
 * Entidad CITA.
 * PK: idCita
 * FK: idMascota -> MASCOTA.idMascota (otro microservicio, se resuelve via Feign)
 */
@Entity
@Table(name = "CITA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Este objeto representa una cita veterinaria dentro del sistema.")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCita", nullable = false, unique = true)
    @Schema(description = "Identificador unico de la cita", example = "1")
    private Integer idCita;

    @Column(name = "idMascota", nullable = false)
    @Schema(description = "Identificador de la mascota", example = "1")
    private Integer idMascota;

    @Column(name = "fecha", nullable = false)
    @Schema(description = "Fecha de la cita", example = "2026-04-10")
    private LocalDate fecha;

    @Column(name = "hora", length = 5, nullable = false)
    @Schema(description = "Hora de la cita", example = "10:30")
    private String hora;

    @Column(name = "motivo", length = 100, nullable = false)
    @Schema(description = "Motivo de la cita", example = "Control anual")
    private String motivo;

    @Column(name = "veterinario", length = 50)
    @Schema(description = "Veterinario que atiende", example = "Dra. Rojas")
    private String veterinario;

    @Column(name = "estado", length = 20, nullable = false)
    @Schema(description = "Estado de la cita", example = "PENDIENTE")
    private String estado;
}
