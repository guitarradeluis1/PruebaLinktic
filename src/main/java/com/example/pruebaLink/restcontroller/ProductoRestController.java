package com.example.pruebaLink.restcontroller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
public class ProductoRestController {

    @GetMapping("/estado")
    public String inicio(){
        return "API OK";
    }
}
