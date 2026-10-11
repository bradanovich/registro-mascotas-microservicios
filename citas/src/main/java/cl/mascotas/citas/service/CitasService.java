package cl.mascotas.citas.service;

import cl.mascotas.citas.client.MascotaClient;
import cl.mascotas.citas.dto.MascotaDTO;
import cl.mascotas.citas.model.Cita;
import cl.mascotas.citas.repository.CitasRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CitasService {
    @Autowired
    private CitasRepository cr;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Cita> listar() {
        return cr.findAll();
    }

    public Cita buscarPorId(Integer id) {
        return cr.findById(id)
        .orElseThrow(()-> new RuntimeException("La cita no existe."));
    }

    public Cita guardarCita(Cita cita) {
        MascotaDTO mascotaDTO = mascotaClient.buscarMascotaPorId(cita.getIdMascota());
        if (mascotaDTO == null) {
            throw new RuntimeException("La mascota no existe.");
        }
        return cr.save(cita);
    }

    public Cita actualizarCita(Integer id, Cita cita) {
        Cita c = buscarPorId(id);
        cita.setIdCita(c.getIdCita());
        return cr.save(cita);
    }

    public boolean eliminarCita(Integer id) {
        Cita c = buscarPorId(id);
        if (c != null) {
            cr.delete(c);
            return true;
        }
        return false;
    }
}
