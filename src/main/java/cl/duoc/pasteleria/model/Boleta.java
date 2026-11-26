package cl.duoc.pasteleria.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Boleta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Usuario usuario;

    private LocalDateTime fecha;
    private String direccion;
    private int subtotal;
    private int descuento;
    private int total;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetalleBoleta> detalles = new ArrayList<>();
}
