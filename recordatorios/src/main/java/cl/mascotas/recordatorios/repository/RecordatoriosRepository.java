package cl.mascotas.recordatorios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.mascotas.recordatorios.model.Recordatorio;

@Repository
public interface RecordatoriosRepository extends JpaRepository<Recordatorio, Integer> {

}
