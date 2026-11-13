package cl.mascotas.usuarios.service;

import cl.mascotas.usuarios.dto.UsuarioDTO;
import cl.mascotas.usuarios.model.Usuario;
import cl.mascotas.usuarios.repository.UsuariosRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UsuariosService {

    private final UsuariosRepository ur;

    // Inyeccion de dependencias por constructor
    public UsuariosService(UsuariosRepository ur) {
        this.ur = ur;
    }

    // Listar todos los usuarios
    public List<Usuario> listar() {
        return ur.findAll();
    }

    // Buscar usuario por ID
    public Usuario buscarPorId(Integer id) {
        return ur.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("El usuario no existe."));
    }

    // Registrar nuevo usuario
    public Usuario registrar(Usuario usuario) {

        // Evita registrar dos usuarios con el mismo email
        if (ur.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya esta registrado.");
        }

        return ur.save(usuario);
    }

    // Login
    public Usuario login(String email, String password) {

        Usuario usuario = ur.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Credenciales incorrectas."));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Credenciales incorrectas.");
        }

        return usuario;
    }

    // Actualizar usuario
    public Usuario actualizar(Integer id, Usuario datosUsuario) {

        Usuario usuarioActual = buscarPorId(id);

        /*
         * Si el usuario intenta cambiar su email,
         * comprobamos que el nuevo correo no pertenezca
         * a otra cuenta.
         */
        if (!usuarioActual.getEmail().equals(datosUsuario.getEmail())
                && ur.existsByEmail(datosUsuario.getEmail())) {

            throw new RuntimeException(
                    "El email ya esta registrado por otro usuario.");
        }

        usuarioActual.setEmail(datosUsuario.getEmail());
        usuarioActual.setPassword(datosUsuario.getPassword());
        usuarioActual.setNombre(datosUsuario.getNombre());
        usuarioActual.setApellido(datosUsuario.getApellido());

        return ur.save(usuarioActual);
    }

    // Eliminar usuario
    public void eliminar(Integer id) {

        Usuario usuario = buscarPorId(id);

        ur.delete(usuario);
    }

    // Buscar usuario utilizando DTO
    public UsuarioDTO buscarUsuarioDTO(Integer id) {

        Usuario usuario = buscarPorId(id);

        UsuarioDTO dto = new UsuarioDTO();

        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());

        return dto;
    }
}