package mx.edu.itoaxaca.smaact3.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.edu.itoaxaca.smaact3.models.entity.Carrera;
import mx.edu.itoaxaca.smaact3.service.CarreraService;

/**
 * Controlador MVC del catalogo de Carreras (regresa vistas Thymeleaf).
 */
@Controller
@RequestMapping("/carreras")
public class CarreraController {

    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("carreras", carreraService.listar());
        return "carreras/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("carrera", new Carrera());
        model.addAttribute("titulo", "Registrar carrera");
        return "carreras/formulario";
    }

    @PostMapping
    public String crear(@ModelAttribute("carrera") Carrera carrera, RedirectAttributes redirect) {
        carreraService.crear(carrera);
        redirect.addFlashAttribute("mensaje", "Carrera registrada correctamente.");
        return "redirect:/carreras";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model,
                                   RedirectAttributes redirect) {
        return carreraService.buscarPorId(id).map(carrera -> {
            model.addAttribute("carrera", carrera);
            model.addAttribute("titulo", "Editar carrera");
            return "carreras/formulario";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("error", "No existe la carrera con id " + id + ".");
            return "redirect:/carreras";
        });
    }

    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Integer id, @ModelAttribute("carrera") Carrera carrera,
                             RedirectAttributes redirect) {
        if (carreraService.actualizar(id, carrera).isPresent()) {
            redirect.addFlashAttribute("mensaje", "Carrera actualizada correctamente.");
        } else {
            redirect.addFlashAttribute("error", "No existe la carrera con id " + id + ".");
        }
        return "redirect:/carreras";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            if (carreraService.eliminar(id)) {
                redirect.addFlashAttribute("mensaje", "Carrera eliminada correctamente.");
            } else {
                redirect.addFlashAttribute("error", "No existe la carrera con id " + id + ".");
            }
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carreras";
    }
}
