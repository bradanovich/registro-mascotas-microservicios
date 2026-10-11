package cl.mascotas.salud.service;

import cl.mascotas.salud.client.MascotaClient;
import cl.mascotas.salud.dto.MascotaDTO;
import cl.mascotas.salud.model.Control;
import cl.mascotas.salud.repository.ControlesRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ControlesService {
    @Autowired
    private ControlesRepository cr;

    @Autowired
    private MascotaClient mascotaClient;

    public List<Control> listar() {
        return cr.findAll();
    }

    public Control buscarPorId(Integer id) {
        return cr.findById(id)
        .orElseThrow(()-> new RuntimeException("El control no existe."));
    }

    public Control guardarControl(Control control) {
        MascotaDTO mascotaDTO = mascotaClient.buscarMascotaPorId(control.getIdMascota());
        if (mascotaDTO == null) {
            throw new RuntimeException("La mascota no existe.");
        }
        return cr.save(control);
    }

    public Control actualizarControl(Integer id, Control control) {
        Control c = buscarPorId(id);
        control.setIdControl(c.getIdControl());
        return cr.save(control);
    }

    public boolean eliminarControl(Integer id) {
        Control c = buscarPorId(id);
        if (c != null) {
            cr.delete(c);
            return true;
        }
        return false;
    }
}
