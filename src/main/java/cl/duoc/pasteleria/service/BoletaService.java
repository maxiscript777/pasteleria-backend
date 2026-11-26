package cl.duoc.pasteleria.service;

import cl.duoc.pasteleria.dto.CompraRequest;
import cl.duoc.pasteleria.model.*;
import cl.duoc.pasteleria.repository.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BoletaService {
    private final BoletaRepository repo;
    private final UsuarioRepository usuarios;
    private final ProductoService productos;

    public List<Boleta> listar() {
        return repo.findAll();
    }

    public List<Boleta> listarMias(String correo) {
        return repo.findByUsuarioCorreoOrderByFechaDesc(correo);
    }

    public Boleta buscar(Long id, Authentication auth) {
        Boleta b = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Boleta no existe"));
        boolean esPersonal = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_VENDEDOR"));
        if (!esPersonal && !b.getUsuario().getCorreo().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esa boleta no es tuya");
        }
        return b;
    }

    // si algo falla se deshace todo
    @Transactional
    public Boleta comprar(String correo, CompraRequest compra) {
        Usuario usuario = usuarios.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no existe"));
        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setFecha(LocalDateTime.now());
        boleta.setDireccion(compra.direccion());

        int subtotal = 0;
        for (CompraRequest.Item item : compra.items()) {
            Producto p = productos.buscar(item.productoId());
            if (p.getStock() < item.cantidad()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay stock de " + p.getNombre());
            }
            p.setStock(p.getStock() - item.cantidad());

            DetalleBoleta d = new DetalleBoleta();
            d.setBoleta(boleta);
            d.setProducto(p);
            d.setCantidad(item.cantidad());
            d.setPrecio(p.precioFinal());
            boleta.getDetalles().add(d);
            subtotal += d.getPrecio() * d.getCantidad();
        }

        // codigo felices50 = 10% de descuento
        int descuento = "FELICES50".equalsIgnoreCase(compra.codigo()) ? subtotal / 10 : 0;
        boleta.setSubtotal(subtotal);
        boleta.setDescuento(descuento);
        boleta.setTotal(subtotal - descuento);
        return repo.save(boleta);
    }
}
