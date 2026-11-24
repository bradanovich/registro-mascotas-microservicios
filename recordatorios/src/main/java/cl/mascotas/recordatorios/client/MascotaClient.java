package cl.mascotas.recordatorios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.mascotas.recordatorios.dto.MascotaDTO;

@FeignClient(name = "mascotas")
public interface MascotaClient {

    @GetMapping("/mascotas-app/mascotas/dto/{id}")
    MascotaDTO buscarMascotaPorId(@PathVariable("id") Integer id);

}
