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

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad CONTROL.
 * PK: idControl
 * FK: idMascota -> MASCOTA.idMascota (otro microservicio, se resuelve via Feign)
 */
@Entity
@Table(name = "CONTROL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Este objeto representa un control veterinario de una mascota.")
public class Control {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idControl", nullable = false, unique = true)
    @Schema(description = "Identificador unico del control", example = "1")
    private Integer idControl;

    @Column(name = "idMascota", nullable = false)
    @Schema(description = "Identificador de la mascota", example = "1")
    private Integer idMascota;

    @Column(name = "fecha", nullable = false)
    @Schema(description = "Fecha del control", example = "2026-04-01")
    private LocalDate fecha;

    @Column(name = "peso", precision = 5, scale = 2)
    @Schema(description = "Peso registrado en kg", example = "12.50")
    private BigDecimal peso;

    @Column(name = "temperatura", precision = 4, scale = 1)
    @Schema(description = "Temperatura corporal en Celsius", example = "38.5")
    private BigDecimal temperatura;

    @Column(name = "observaciones", length = 255)
    @Schema(description = "Observaciones del control", example = "Estado general bueno")
    private String observaciones;
}
