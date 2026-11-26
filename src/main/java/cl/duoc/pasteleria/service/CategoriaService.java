package cl.duoc.pasteleria.service;

import cl.duoc.pasteleria.model.Categoria;
import cl.duoc.pasteleria.repository.CategoriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository repo;

    public List<Categoria> listar() {
        return repo.findAll();
    }

    public Categoria buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no existe"));
    }

    public Categoria crear(Categoria c) {
        c.setId(null);
        return repo.save(c);
    }

    public Categoria actualizar(Long id, Categoria c) {
        buscar(id);
        c.setId(id);
        return repo.save(c);
    }

    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }
}
