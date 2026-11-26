package cl.duoc.pasteleria.service;

import cl.duoc.pasteleria.model.Producto;
import cl.duoc.pasteleria.repository.ProductoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository repo;
    private final CategoriaService categorias;

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Producto buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no existe"));
    }

    public Producto crear(Producto p) {
        p.setId(null);
        p.setCategoria(categorias.buscar(p.getCategoria().getId()));
        return repo.save(p);
    }

    public Producto actualizar(Long id, Producto p) {
        buscar(id);
        p.setId(id);
        p.setCategoria(categorias.buscar(p.getCategoria().getId()));
        return repo.save(p);
    }

    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }
}
