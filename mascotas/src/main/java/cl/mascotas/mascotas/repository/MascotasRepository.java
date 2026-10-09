package cl.mascotas.mascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mascotas.mascotas.model.Mascota;

@Repository
public interface MascotasRepository extends JpaRepository<Mascota, Integer> {

}
