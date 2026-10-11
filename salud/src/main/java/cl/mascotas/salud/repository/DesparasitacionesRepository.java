package cl.mascotas.salud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mascotas.salud.model.Desparasitacion;

@Repository
public interface DesparasitacionesRepository extends JpaRepository<Desparasitacion, Integer> {

}
