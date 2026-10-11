package cl.mascotas.recordatorios.model;

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
 * Entidad RECORDATORIO.
 * PK: idRecordatorio
 * FK: idMascota -> MASCOTA.idMascota (otro microservicio, se resuelve via Feign)
 */
@Entity
@Table(name = "RECORDATORIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Este objeto representa un aviso o recordatorio dentro del sistema.")
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRecordatorio", nullable = false, unique = true)
    @Schema(description = "Identificador unico del recordatorio", example = "1")
    private Integer idRecordatorio;

    @Column(name = "idMascota", nullable = false)
    @Schema(description = "Identificador de la mascota", example = "1")
    private Integer idMascota;

    @Column(name = "titulo", length = 50, nullable = false)
    @Schema(description = "Titulo del recordatorio", example = "Vacuna antirrabica")
    private String titulo;

    @Column(name = "mensaje", length = 255, nullable = false)
    @Schema(description = "Mensaje del recordatorio", example = "Rex tiene vacuna pendiente el 15 de abril")
    private String mensaje;

    @Column(name = "fechaEnvio", nullable = false)
    @Schema(description = "Fecha programada de envio", example = "2026-04-14")
    private LocalDate fechaEnvio;

    @Column(name = "estado", length = 20, nullable = false)
    @Schema(description = "Estado del recordatorio", example = "PENDIENTE")
    private String estado;
}
