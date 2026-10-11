package cl.mascotas.citas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mascotas.citas.model.Cita;

@Repository
public interface CitasRepository extends JpaRepository<Cita, Integer> {

}
