package cl.mascotas.salud.service;

import cl.mascotas.salud.client.MascotaClient;
import cl.mascotas.salud.dto.MascotaDTO;
import cl.mascotas.salud.model.Desparasitacion;
import cl.mascotas.salud.repository.DesparasitacionesRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DesparasitacionesService {
    @Autowired
    private DesparasitacionesRepository dr;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Desparasitacion> listar() {
        return dr.findAll();
    }

    public Desparasitacion buscarPorId(Integer id) {
        return dr.findById(id)
        .orElseThrow(()-> new RuntimeException("La desparasitacion no existe."));
    }

    public Desparasitacion guardarDesparasitacion(Desparasitacion desparasitacion) {
        MascotaDTO mascotaDTO = mascotaClient.buscarMascotaPorId(desparasitacion.getIdMascota());
        if (mascotaDTO == null) {
            throw new RuntimeException("La mascota no existe.");
        }
        return dr.save(desparasitacion);
    }

    public Desparasitacion actualizarDesparasitacion(Integer id, Desparasitacion desparasitacion) {
        Desparasitacion d = buscarPorId(id);
        desparasitacion.setIdDesparasitacion(d.getIdDesparasitacion());
        return dr.save(desparasitacion);
    }

    public boolean eliminarDesparasitacion(Integer id) {
        Desparasitacion d = buscarPorId(id);
        if (d != null) {
            dr.delete(d);
            return true;
        }
        return false;
    }
}
