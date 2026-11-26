package cl.duoc.pasteleria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Min(1)
    private int precio;

    private boolean oferta;
    private int precioOferta;

    @Min(0)
    private int stock;

    private String imagen;

    @ManyToOne
    @NotNull
    private Categoria categoria;

    public int precioFinal() {
        return oferta ? precioOferta : precio;
    }
}
