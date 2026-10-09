package cl.mascotas.mascotas.client;

import cl.mascotas.mascotas.dto.UsuarioDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios")
public interface UsuarioClient {

    @GetMapping("/mascotas-app/usuarios/dto/{id}")
    UsuarioDTO buscarUsuarioPorId(@PathVariable("id") Integer id);
}