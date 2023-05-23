package com.chatbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saludo")
public class Controlador {

        @GetMapping("/hola")
        public String hola() {
            return "Hola mundo";
        }

        @GetMapping("/adios")
        public String adios() {
            return "Adios mundo";
        }
}
