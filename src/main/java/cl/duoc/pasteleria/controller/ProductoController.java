package cl.duoc.pasteleria.controller;

import cl.duoc.pasteleria.model.Producto;
import cl.duoc.pasteleria.service.ProductoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService service;

    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@Valid @RequestBody Producto x) {
        return service.crear(x);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody Producto x) {
        return service.actualizar(id, x);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
