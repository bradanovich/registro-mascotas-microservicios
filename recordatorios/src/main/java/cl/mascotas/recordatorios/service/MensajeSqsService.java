package cl.mascotas.recordatorios.service;

import cl.mascotas.recordatorios.model.Recordatorio;
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
            System.getenv("RECORDATORIOS_QUEUE_URL");

    private SqsClient sqs;

    public void publicarRecordatorioCreado(Recordatorio recordatorio) {

        Map<String, Object> mensaje = new LinkedHashMap<>();
        mensaje.put("evento", "RECORDATORIO_CREADO");
        mensaje.put(
                "idRecordatorio",
                recordatorio.getIdRecordatorio()
        );
        mensaje.put("idMascota", recordatorio.getIdMascota());
        mensaje.put("titulo", recordatorio.getTitulo());
        mensaje.put("mensaje", recordatorio.getMensaje());
        mensaje.put(
                "fechaEnvio",
                recordatorio.getFechaEnvio() == null
                        ? null
                        : recordatorio.getFechaEnvio().toString()
        );
        mensaje.put("estado", recordatorio.getEstado());

        enviar(mensaje);
    }

    private void enviar(Map<String, Object> mensaje) {

        if (COLA == null || COLA.isBlank()) {
            LOG.warn(
                    "Mensaje no enviado a SQS: falta la variable de entorno RECORDATORIOS_QUEUE_URL"
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
