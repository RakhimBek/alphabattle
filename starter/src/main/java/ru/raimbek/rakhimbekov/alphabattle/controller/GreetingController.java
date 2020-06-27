package ru.raimbek.rakhimbekov.alphabattle.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String hi(String request) {
        return String.format("Hello, World(%s)!", request);
    }
}