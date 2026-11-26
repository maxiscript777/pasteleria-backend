package cl.duoc.pasteleria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Contacto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank @Email
    private String correo;

    @NotBlank @Size(max = 500)
    private String mensaje;

    private LocalDateTime fecha;
}
