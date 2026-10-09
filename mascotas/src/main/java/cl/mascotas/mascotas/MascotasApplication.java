package cl.mascotas.mascotas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MascotasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MascotasApplication.class, args);
	}

}
