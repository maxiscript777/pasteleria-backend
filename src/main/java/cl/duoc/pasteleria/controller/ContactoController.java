package cl.duoc.pasteleria.controller;

import cl.duoc.pasteleria.model.Contacto;
import cl.duoc.pasteleria.repository.ContactoRepository;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contacto")
@RequiredArgsConstructor
public class ContactoController {
    private final ContactoRepository repo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contacto enviar(@Valid @RequestBody Contacto c) {
        c.setId(null);
        c.setFecha(LocalDateTime.now());
        return repo.save(c);
    }

    @GetMapping
    public List<Contacto> listar() {
        return repo.findAll();
    }
}
