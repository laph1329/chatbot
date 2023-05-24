package com.chatbot.controller;

import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/saludo")
public class Controlador {
    private static final Logger logger = LoggerFactory.getLogger(Controlador.class);
    @GetMapping("/hola")
    public ResponseEntity<String> hola() {
        return ResponseEntity.status(HttpStatus.OK).body("Hola mundo");
    }

    RestTemplate restTemplate = new RestTemplate();
    @Value("${token}")
    private String token;
    private String phone_number_id = "100940642691350";
    private String from = "51963455195";
    private String msg_body = "Hola desde el webhook";
    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody Map<String,Object> payloadObject) {
        JSONObject payload = new JSONObject(payloadObject);
        try {
            System.out.println("*****payload*****");
            logger.info(payload.toString());
            System.out.println("*****FIN-payload*****");
            if(!payload.getJSONArray("entry").isEmpty() &&
                    !payload.getJSONArray("entry").getJSONObject(0).getJSONArray("changes").isEmpty() &&
                    !payload.getJSONArray("entry").getJSONObject(0).getJSONArray("changes").getJSONObject(0).isEmpty() &&
                    !payload.getJSONArray("entry").getJSONObject(0).getJSONArray("changes").getJSONObject(0).getJSONObject("value").getJSONArray("messages").isEmpty() &&
                   ! payload.getJSONArray("entry").getJSONObject(0).getJSONArray("changes").getJSONObject(0).getJSONObject("value").getJSONArray("messages").getJSONObject(0).isEmpty()
            ) {
                String url = "https://graph.facebook.com/v16.0/" + phone_number_id + "/messages?access_token=" + token;

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                String requestBody = "{\"messaging_product\": \"whatsapp\", \"to\": \"" + from + "\", \"text\": {\"body\": \"Ack: " + msg_body + "\"}}";
                HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                return ResponseEntity.ok().build();
            }
            else{
                logger.info("Aqui no hay mensajes");
                return ResponseEntity.ok(msg_body);
            }
        }catch (Exception e) {
            logger.error("Error al procesar el webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }







//        if (payload.getObject() != null && payload.getEntry() != null && payload.getEntry().size() > 0) {
//            WebhookEntry entry = payload.getEntry().get(0);
//            if (entry.getChanges() != null && entry.getChanges().size() > 0) {
//                WebhookChange change = entry.getChanges().get(0);
//                if (change.getValue().getMessages() != null && change.getValue().getMessages().size() > 0) {
//                    String phoneNumberId = change.getValue().getMetadata().getPhoneNumberId();
//                    String from = change.getValue().getMessages().get(0).getFrom();
//                    String msgBody = change.getValue().getMessages().get(0).getText().getBody();
//                    // Send the response using axios or any other HTTP client
//                    // ...
//                }
//            }
//        }
    }

    @Value("${webhook_token}")
    private String webhookToken;

    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode, @RequestParam("hub.verify_token") String token, @RequestParam("hub.challenge") String challenge) {
        logger.info("*****parametros que envia facebook*****");
        logger.info("mode: " + mode);
        logger.info("token: " + token);
        logger.info("challenge: " + challenge);
        // Update your verify token
        String verifyToken = webhookToken;
        if (mode != null && token != null && mode.equals("subscribe") && token.equals(verifyToken)) {
            logger.info("WEBHOOK VERIFICADO");
            return ResponseEntity.status(HttpStatus.OK).body(challenge);
        } else {
            logger.error("WEBHOOK NO VERIFICADO");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
