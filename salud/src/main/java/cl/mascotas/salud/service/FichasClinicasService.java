package cl.mascotas.salud.service;

import cl.mascotas.salud.client.MascotaClient;
import cl.mascotas.salud.dto.MascotaDTO;
import cl.mascotas.salud.model.FichaClinica;
import cl.mascotas.salud.repository.FichasClinicasRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FichasClinicasService {
    @Autowired
    private FichasClinicasRepository fr;

    @Autowired
    private MascotaClient mascotaClient;

    public List<FichaClinica> listar() {
        return fr.findAll();
    }

    public FichaClinica buscarPorId(Integer id) {
        return fr.findById(id)
        .orElseThrow(()-> new RuntimeException("La ficha clinica no existe."));
    }

    public FichaClinica guardarFichaClinica(FichaClinica ficha) {
        MascotaDTO mascotaDTO = mascotaClient.buscarMascotaPorId(ficha.getIdMascota());
        if (mascotaDTO == null) {
            throw new RuntimeException("La mascota no existe.");
        }
        return fr.save(ficha);
    }

    public FichaClinica actualizarFichaClinica(Integer id, FichaClinica ficha) {
        FichaClinica f = buscarPorId(id);
        ficha.setIdFicha(f.getIdFicha());
        return fr.save(ficha);
    }

    public boolean eliminarFichaClinica(Integer id) {
        FichaClinica f = buscarPorId(id);
        if (f != null) {
            fr.delete(f);
            return true;
        }
        return false;
    }
}
