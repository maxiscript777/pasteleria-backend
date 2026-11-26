package cl.duoc.pasteleria.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class DetalleBoleta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Boleta boleta;

    @ManyToOne(optional = false)
    private Producto producto;

    private int cantidad;
    private int precio;
}
