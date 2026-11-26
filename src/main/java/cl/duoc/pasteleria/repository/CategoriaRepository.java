package cl.duoc.pasteleria.repository;

import cl.duoc.pasteleria.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
