package cl.duoc.pasteleria.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;

    @NotBlank @Email
    @Column(unique = true)
    private String correo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clave;

    @Pattern(regexp = "ADMIN|VENDEDOR|CLIENTE")
    private String rol;
}
