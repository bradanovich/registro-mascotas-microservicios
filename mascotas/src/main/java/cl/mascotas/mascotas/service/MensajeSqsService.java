package cl.mascotas.mascotas.service;

import cl.mascotas.mascotas.model.Mascota;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MensajeSqsService {

    private static final Logger LOG =
            LoggerFactory.getLogger(MensajeSqsService.class);

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final String COLA =
            System.getenv("MASCOTAS_QUEUE_URL");

    private SqsClient sqs;

    public void publicarMascotaCreada(Mascota mascota) {

        Map<String, Object> mensaje = new LinkedHashMap<>();
        mensaje.put("evento", "MASCOTA_CREADA");
        mensaje.put("idMascota", mascota.getIdMascota());
        mensaje.put("nombre", mascota.getNombre());
        mensaje.put("especie", mascota.getEspecie());
        mensaje.put("raza", mascota.getRaza());
        mensaje.put(
                "fechaNacimiento",
                mascota.getFechaNacimiento() == null
                        ? null
                        : mascota.getFechaNacimiento().toString()
        );
        mensaje.put("idDueno", mascota.getIdDueno());

        enviar(mensaje);
    }

    private void enviar(Map<String, Object> mensaje) {

        if (COLA == null || COLA.isBlank()) {
            LOG.warn(
                    "Mensaje no enviado a SQS: falta la variable de entorno MASCOTAS_QUEUE_URL"
            );
            return;
        }

        String pruebaId = UUID.randomUUID().toString();
        mensaje.put("pruebaId", pruebaId);
        mensaje.put("fechaEvento", LocalDateTime.now().toString());

        try {

            String cuerpo = JSON.writeValueAsString(mensaje);

            SendMessageResponse respuesta = obtenerCliente().sendMessage(
                    solicitud -> solicitud
                            .queueUrl(COLA)
                            .messageBody(cuerpo)
            );

            LOG.info(
                    "Mensaje SQS enviado pruebaId={} messageId={}",
                    pruebaId,
                    respuesta.messageId()
            );

        } catch (Exception e) {

            LOG.error(
                    "No fue posible enviar el mensaje SQS pruebaId={}: {}",
                    pruebaId,
                    e.getMessage()
            );
        }
    }

    private synchronized SqsClient obtenerCliente() {

        if (sqs == null) {
            sqs = SqsClient.create();
        }

        return sqs;
    }

    @PreDestroy
    public void cerrar() {

        if (sqs != null) {
            sqs.close();
        }
    }
}
