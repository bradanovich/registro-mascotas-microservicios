package cl.mascotas.mascotas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad MASCOTA.
 * PK: idMascota
 * FK logica: idDueno -> USUARIO.idUsuario
 * La relacion con Usuario se resuelve mediante otro microservicio.
 */
@Entity
@Table(name = "MASCOTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Este objeto representa a una mascota dentro del sistema.")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMascota", nullable = false, unique = true)
    @Schema(
            description = "Identificador unico de la mascota",
            example = "1"
    )
    private Integer idMascota;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Column(name = "nombre", length = 30, nullable = false)
    @Schema(
            description = "Nombre de la mascota",
            example = "Rex"
    )
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Column(name = "especie", length = 20, nullable = false)
    @Schema(
            description = "Especie de la mascota",
            example = "Perro"
    )
    private String especie;

    @Column(name = "raza", length = 30)
    @Schema(
            description = "Raza de la mascota",
            example = "Labrador"
    )
    private String raza;

    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    @Column(name = "fechaNacimiento")
    @Schema(
            description = "Fecha de nacimiento de la mascota",
            example = "2022-05-10"
    )
    private LocalDate fechaNacimiento;

    @NotNull(message = "El identificador del dueno es obligatorio")
    @Column(name = "idDueno", nullable = false)
    @Schema(
            description = "Identificador del dueno de la mascota",
            example = "1"
    )
    private Integer idDueno;
}