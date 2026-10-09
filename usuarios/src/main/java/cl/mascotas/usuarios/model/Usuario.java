package cl.mascotas.usuarios.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad USUARIO.
 * PK: idUsuario
 * Representa al dueño de mascotas que usa la aplicacion.
 */
@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Este objeto representa a un usuario dentro del sistema.")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario", nullable = false, unique = true)
    @Schema(description = "Identificador unico del usuario", example = "1")
    private Integer idUsuario;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    @Column(name = "email", length = 50, nullable = false, unique = true)
    @Schema(description = "Email del usuario usado para login", example = "juan@email.com")
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 4, max = 100, message = "La contrasena debe tener entre 4 y 100 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 100, nullable = false)
    @Schema(description = "Contrasena del usuario", example = "1234")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", length = 30, nullable = false)
    @Schema(description = "Primer nombre del usuario", example = "Juan")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(name = "apellido", length = 30, nullable = false)
    @Schema(description = "Apellido del usuario", example = "Gonzalez")
    private String apellido;
}