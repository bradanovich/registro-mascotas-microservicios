package cl.mascotas.citas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.mascotas.citas.dto.MascotaDTO;

@FeignClient(name = "mascotas", url = "http://localhost:8082")
public interface MascotaClient {

    @GetMapping("/mascotas-app/mascotas/dto/{id}")
    MascotaDTO buscarMascotaPorId(@PathVariable("id") Integer id);

}
