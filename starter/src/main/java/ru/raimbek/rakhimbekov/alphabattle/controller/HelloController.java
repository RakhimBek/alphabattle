package ru.raimbek.rakhimbekov.alphabattle.controller;

import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raimbek.rakhimbekov.alphabattle.dto.Status;

@CrossOrigin("*")
@RestController
@RequestMapping("api")
public class HelloController {

    @GetMapping(value = "/hello", produces = "application/json")
    public String hello() {
        final Status status = new Status();
        status.setMessage("Hello, World!");

        return GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays()
                .setPrettyPrinting()
                .create()
                .toJson(status);
    }
}
