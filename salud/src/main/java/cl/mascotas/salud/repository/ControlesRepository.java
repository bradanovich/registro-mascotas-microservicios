package cl.mascotas.salud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mascotas.salud.model.Control;

@Repository
public interface ControlesRepository extends JpaRepository<Control, Integer> {

}
