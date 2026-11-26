package cl.duoc.pasteleria.config;

import cl.duoc.pasteleria.model.*;
import cl.duoc.pasteleria.repository.*;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// carga datos de ejemplo si la base esta vacia
@Component
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {
    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;
    private final ProductoRepository productos;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (usuarios.count() > 0) {
            return;
        }
        usuario("Admin", "Tienda", "admin@duoc.cl", "admin123", "ADMIN");
        usuario("Vendedor", "Tienda", "vendedor@duoc.cl", "vendedor1", "VENDEDOR");
        usuario("Cliente", "Demo", "cliente@gmail.com", "cliente1", "CLIENTE");

        Map<String, Categoria> cat = new HashMap<>();
        cat.put("Tortas cuadradas", categoria("Tortas cuadradas"));
        cat.put("Tortas circulares", categoria("Tortas circulares"));
        cat.put("Postres individuales", categoria("Postres individuales"));
        cat.put("Productos sin azucar", categoria("Productos sin azucar"));
        cat.put("Productos veganos", categoria("Productos veganos"));
        cat.put("Pasteleria tradicional", categoria("Pasteleria tradicional"));
        cat.put("Productos sin gluten", categoria("Productos sin gluten"));
        cat.put("Tortas especiales", categoria("Tortas especiales"));

        producto(cat.get("Tortas cuadradas"), "Torta cuadrada de chocolate", 45000, 40000, 10, "/img/torta_cuadrada_choc.jpg",
                "Torta de chocolate con capas de crema y avellanas.");
        producto(cat.get("Tortas cuadradas"), "Torta cuadrada de frutas", 50000, 0, 8, "/img/torta_cua_frutas.jpg",
                "Frutas frescas y crema chantilly sobre un bizcocho suave.");
        producto(cat.get("Tortas circulares"), "Torta circular de vainilla", 40000, 35000, 10, "/img/torta_circ_vainilla.jpg",
                "Bizcocho de vainilla relleno con crema pastelera.");
        producto(cat.get("Tortas circulares"), "Torta circular de manjar", 42000, 0, 2, "/img/torta_circ_manjar.jpg",
                "Torta tradicional chilena con manjar y nueces.");
        producto(cat.get("Postres individuales"), "Mousse de chocolate", 5000, 0, 20, "/img/mousse_choc.jpg",
                "Postre cremoso de chocolate para una persona.");
        producto(cat.get("Postres individuales"), "Tiramisu clasico", 5500, 4500, 15, "/img/tiramisu.jpg",
                "Postre italiano con cafe y queso mascarpone.");
        producto(cat.get("Productos sin azucar"), "Torta sin azucar de naranja", 48000, 0, 5, "/img/torta_naranja.jpg",
                "Torta liviana de naranja, sin azucar.");
        producto(cat.get("Productos veganos"), "Torta vegana de chocolate", 50000, 0, 1, "/img/torta_veg_choc.jpg",
                "Torta de chocolate sin leche ni huevos.");
        producto(cat.get("Productos sin azucar"), "Cheesecake sin azucar", 47000, 0, 8, "/img/psa002-cheesecake.jpg",
                "Suave y cremoso, una opcion perfecta para disfrutar sin culpa.");
        producto(cat.get("Pasteleria tradicional"), "Empanada de manzana", 3000, 0, 25, "/img/pt001-empanada.jpg",
                "Pasteleria tradicional rellena de manzanas especiadas, perfecta para un dulce desayuno o merienda.");
        producto(cat.get("Pasteleria tradicional"), "Tarta de Santiago", 6000, 0, 12, "/img/pt002-tarta-santiago.jpg",
                "Tradicional tarta española hecha con almendras, azucar y huevos, una delicia para los amantes de los postres clasicos.");
        producto(cat.get("Productos sin gluten"), "Brownie sin gluten", 4000, 0, 18, "/img/pg001-brownie.jpg",
                "Rico y denso, perfecto para quienes necesitan evitar el gluten sin sacrificar el sabor.");
        producto(cat.get("Productos sin gluten"), "Pan sin gluten", 3500, 0, 15, "/img/pg002-pan.jpg",
                "Suave y esponjoso, ideal para sandwiches o para acompañar cualquier comida.");
        producto(cat.get("Productos veganos"), "Galletas veganas de avena", 4500, 0, 20, "/img/pv002-galletas.jpg",
                "Crujientes y sabrosas, una excelente opcion para un snack saludable y vegano.");
        producto(cat.get("Tortas especiales"), "Torta especial de cumpleaños", 55000, 0, 6, "/img/te001-cumpleanos.jpg",
                "Diseñada especialmente para celebraciones, personalizable con decoraciones y mensajes unicos.");
        producto(cat.get("Tortas especiales"), "Torta especial de boda", 60000, 0, 6, "/img/te002-boda.jpg",
                "Elegante y deliciosa, diseñada para ser el centro de atencion en cualquier boda.");
    }

    private void usuario(String nombre, String apellidos, String correo, String clave, String rol) {
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setApellidos(apellidos);
        u.setCorreo(correo);
        u.setClave(encoder.encode(clave));
        u.setRol(rol);
        usuarios.save(u);
    }

    private Categoria categoria(String nombre) {
        Categoria c = new Categoria();
        c.setNombre(nombre);
        return categorias.save(c);
    }

    // precioOferta 0 = sin oferta
    private void producto(Categoria categoria, String nombre, int precio, int precioOferta, int stock, String imagen,
                          String descripcion) {
        Producto p = new Producto();
        p.setCategoria(categoria);
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setOferta(precioOferta > 0);
        p.setPrecioOferta(precioOferta);
        p.setStock(stock);
        p.setImagen(imagen);
        p.setDescripcion(descripcion);
        productos.save(p);
    }
}
