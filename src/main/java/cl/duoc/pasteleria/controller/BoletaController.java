package cl.duoc.pasteleria.controller;

import cl.duoc.pasteleria.dto.CompraRequest;
import cl.duoc.pasteleria.model.Boleta;
import cl.duoc.pasteleria.service.BoletaService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boletas")
@RequiredArgsConstructor
public class BoletaController {
    private final BoletaService service;

    @GetMapping
    public List<Boleta> listar() {
        return service.listar();
    }

    @GetMapping("/mias")
    public List<Boleta> mias(Authentication auth) {
        return service.listarMias(auth.getName());
    }

    @GetMapping("/{id}")
    public Boleta buscar(@PathVariable Long id, Authentication auth) {
        return service.buscar(id, auth);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Boleta comprar(@Valid @RequestBody CompraRequest compra, Authentication auth) {
        return service.comprar(auth.getName(), compra);
    }
}
