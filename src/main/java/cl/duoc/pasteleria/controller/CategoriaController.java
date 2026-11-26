package cl.duoc.pasteleria.controller;

import cl.duoc.pasteleria.model.Categoria;
import cl.duoc.pasteleria.service.CategoriaService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService service;

    @GetMapping
    public List<Categoria> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Categoria buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crear(@Valid @RequestBody Categoria x) {
        return service.crear(x);
    }

    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Long id, @Valid @RequestBody Categoria x) {
        return service.actualizar(id, x);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
