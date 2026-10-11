package cl.mascotas.salud.service;

import cl.mascotas.salud.client.MascotaClient;
import cl.mascotas.salud.dto.MascotaDTO;
import cl.mascotas.salud.model.Vacuna;
import cl.mascotas.salud.repository.VacunasRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class VacunasService {
    @Autowired
    private VacunasRepository vr;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Vacuna> listar() {
        return vr.findAll();
    }

    public Vacuna buscarPorId(Integer id) {
        return vr.findById(id)
        .orElseThrow(()-> new RuntimeException("La vacuna no existe."));
    }

    public Vacuna guardarVacuna(Vacuna vacuna) {
        MascotaDTO mascotaDTO = mascotaClient.buscarMascotaPorId(vacuna.getIdMascota());
        if (mascotaDTO == null) {
            throw new RuntimeException("La mascota no existe.");
        }
        return vr.save(vacuna);
    }

    public Vacuna actualizarVacuna(Integer id, Vacuna vacuna) {
        Vacuna v = buscarPorId(id);
        vacuna.setIdVacuna(v.getIdVacuna());
        return vr.save(vacuna);
    }

    public boolean eliminarVacuna(Integer id) {
        Vacuna v = buscarPorId(id);
        if (v != null) {
            vr.delete(v);
            return true;
        }
        return false;
    }
}
