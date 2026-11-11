package mx.edu.itoaxaca.smaact3.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Al entrar a la raiz del sitio se redirige a la lista de alumnos
    @GetMapping("/")
    public String inicio() {
        return "redirect:/alumnos";
    }
}
