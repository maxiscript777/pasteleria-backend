package cl.duoc.pasteleria.controller;

import cl.duoc.pasteleria.model.Usuario;
import cl.duoc.pasteleria.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crear(@Valid @RequestBody Usuario x) {
        return service.crear(x);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @Valid @RequestBody Usuario x) {
        return service.actualizar(id, x);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
