package cl.duoc.pasteleria.repository;

import cl.duoc.pasteleria.model.Boleta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    List<Boleta> findByUsuarioCorreoOrderByFechaDesc(String correo);
}
