package cl.duoc.pasteleria.service;

import cl.duoc.pasteleria.model.Usuario;
import cl.duoc.pasteleria.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Usuario buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no existe"));
    }

    public Usuario crear(Usuario u) {
        if (u.getClave() == null || u.getClave().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La clave debe tener 6 letras o mas");
        }
        u.setId(null);
        if (u.getRol() == null) {
            u.setRol("CLIENTE");
        }
        u.setClave(encoder.encode(u.getClave()));
        return repo.save(u);
    }

    // el registro siempre crea clientes
    public Usuario registrar(Usuario u) {
        u.setRol("CLIENTE");
        return crear(u);
    }

    public Usuario actualizar(Long id, Usuario datos) {
        Usuario u = buscar(id);
        u.setNombre(datos.getNombre());
        u.setApellidos(datos.getApellidos());
        u.setCorreo(datos.getCorreo());
        if (datos.getRol() != null) {
            u.setRol(datos.getRol());
        }
        if (datos.getClave() != null && !datos.getClave().isBlank()) {
            u.setClave(encoder.encode(datos.getClave()));
        }
        return repo.save(u);
    }

    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }
}
