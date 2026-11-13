package cl.mascotas.mascotas.service;

import cl.mascotas.mascotas.client.UsuarioClient;
import cl.mascotas.mascotas.dto.MascotaDTO;
import cl.mascotas.mascotas.dto.UsuarioDTO;
import cl.mascotas.mascotas.model.Mascota;
import cl.mascotas.mascotas.repository.MascotasRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MascotasService {

    private final MascotasRepository mr;
    private final UsuarioClient usuarioClient;

    // Inyeccion de dependencias por constructor
    public MascotasService(
            MascotasRepository mr,
            UsuarioClient usuarioClient) {

        this.mr = mr;
        this.usuarioClient = usuarioClient;
    }

    // Listar todas las mascotas
    public List<Mascota> listar() {
        return mr.findAll();
    }

    // Buscar mascota por ID
    public Mascota buscarPorId(Integer id) {

        return mr.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("La mascota no existe."));
    }

    // Guardar nueva mascota
    public Mascota guardarMascota(Mascota mascota) {

        validarDueno(mascota.getIdDueno());

        return mr.save(mascota);
    }

    // Actualizar mascota
    public Mascota actualizarMascota(
            Integer id,
            Mascota datosMascota) {

        Mascota mascotaActual = buscarPorId(id);

        // Se valida que el nuevo dueno exista
        validarDueno(datosMascota.getIdDueno());

        mascotaActual.setNombre(datosMascota.getNombre());
        mascotaActual.setEspecie(datosMascota.getEspecie());
        mascotaActual.setRaza(datosMascota.getRaza());
        mascotaActual.setFechaNacimiento(
                datosMascota.getFechaNacimiento()
        );
        mascotaActual.setIdDueno(datosMascota.getIdDueno());

        return mr.save(mascotaActual);
    }

    // Eliminar mascota
    public void eliminarMascota(Integer id) {

        Mascota mascota = buscarPorId(id);

        mr.delete(mascota);
    }

    // Buscar mascota utilizando DTO
    public MascotaDTO buscarMascotaDTO(Integer id) {

        Mascota mascota = buscarPorId(id);

        MascotaDTO dto = new MascotaDTO();

        dto.setIdMascota(mascota.getIdMascota());
        dto.setNombre(mascota.getNombre());

        return dto;
    }

    // Validar que el dueno exista en Usuarios MS
    private void validarDueno(Integer idDueno) {

        try {

            UsuarioDTO usuario =
                    usuarioClient.buscarUsuarioPorId(idDueno);

            if (usuario == null) {
                throw new RuntimeException(
                        "El dueno no existe."
                );
            }

        } catch (FeignException.NotFound e) {

            throw new RuntimeException(
                    "El dueno no existe."
            );

        } catch (FeignException e) {

            throw new RuntimeException(
                    "No fue posible validar el dueno en Usuarios MS."
            );
        }
    }
}