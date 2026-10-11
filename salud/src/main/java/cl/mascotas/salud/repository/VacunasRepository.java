package cl.mascotas.salud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mascotas.salud.model.Vacuna;

@Repository
public interface VacunasRepository extends JpaRepository<Vacuna, Integer> {

}
