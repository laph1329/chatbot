package com.chatbot.controller;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saludo")
public class Controlador {
    private static final Logger logger = LoggerFactory.getLogger(Controlador.class);
    @GetMapping("/hola")
    public ResponseEntity<String> hola() {
        return ResponseEntity.status(HttpStatus.OK).body("Hola mundo");
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody Object payload) {
        System.out.println("*****payload*****");
        logger.info(payload.toString());
        System.out.println("*****FIN-payload*****");
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
        return ResponseEntity.ok().build();
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
