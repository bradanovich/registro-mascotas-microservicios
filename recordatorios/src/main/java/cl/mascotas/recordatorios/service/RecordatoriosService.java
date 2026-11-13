package cl.mascotas.recordatorios.service;

import cl.mascotas.recordatorios.client.MascotaClient;
import cl.mascotas.recordatorios.dto.MascotaDTO;
import cl.mascotas.recordatorios.model.Recordatorio;
import cl.mascotas.recordatorios.repository.RecordatoriosRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RecordatoriosService {
    @Autowired
    private RecordatoriosRepository rr;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Recordatorio> listar() {
        return rr.findAll();
    }

    public Recordatorio buscarPorId(Integer id) {
        return rr.findById(id)
        .orElseThrow(()-> new RuntimeException("El recordatorio no existe."));
    }

    public Recordatorio guardarRecordatorio(Recordatorio recordatorio) {
        MascotaDTO mascotaDTO = mascotaClient.buscarMascotaPorId(recordatorio.getIdMascota());
        if (mascotaDTO == null) {
            throw new RuntimeException("La mascota no existe.");
        }
        return rr.save(recordatorio);
    }

    public Recordatorio actualizarRecordatorio(Integer id, Recordatorio recordatorio) {
        Recordatorio r = buscarPorId(id);
        recordatorio.setIdRecordatorio(r.getIdRecordatorio());
        return rr.save(recordatorio);
    }

    public boolean eliminarRecordatorio(Integer id) {
        Recordatorio r = buscarPorId(id);
        if (r != null) {
            rr.delete(r);
            return true;
        }
        return false;
    }
}
