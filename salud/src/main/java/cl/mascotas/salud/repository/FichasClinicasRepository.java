package cl.mascotas.salud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mascotas.salud.model.FichaClinica;

@Repository
public interface FichasClinicasRepository extends JpaRepository<FichaClinica, Integer> {

}
