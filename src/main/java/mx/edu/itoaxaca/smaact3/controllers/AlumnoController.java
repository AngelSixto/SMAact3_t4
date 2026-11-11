package mx.edu.itoaxaca.smaact3.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.edu.itoaxaca.smaact3.models.entity.Alumno;
import mx.edu.itoaxaca.smaact3.models.entity.Carrera;
import mx.edu.itoaxaca.smaact3.service.AlumnoService;
import mx.edu.itoaxaca.smaact3.service.CarreraService;

/**
 * Controlador MVC del CRUD de Alumnos (regresa vistas Thymeleaf).
 * No tiene logica de negocio: solo recibe la peticion, llama al Service y elige la vista.
 */
@Controller
@RequestMapping("/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final CarreraService carreraService;

    public AlumnoController(AlumnoService alumnoService, CarreraService carreraService) {
        this.alumnoService = alumnoService;
        this.carreraService = carreraService;
    }

    // Lista de carreras disponible en todas las vistas (para el <select> y el filtro)
    @ModelAttribute("carreras")
    public List<Carrera> carreras() {
        return carreraService.listar();
    }

    // ---------- LEER: GET /alumnos  (opcional: ?carreraId=1 para filtrar) ----------
    @GetMapping
    public String listar(@RequestParam(required = false) Integer carreraId, Model model) {
        List<Alumno> alumnos = (carreraId == null)
                ? alumnoService.listar()
                : alumnoService.listarPorCarrera(carreraId);
        model.addAttribute("alumnos", alumnos);
        model.addAttribute("carreraId", carreraId);
        return "alumnos/lista";
    }

    // ---------- CREAR: formulario vacio ----------
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        Alumno alumno = new Alumno();
        alumno.setCarrera(new Carrera());
        model.addAttribute("alumno", alumno);
        model.addAttribute("titulo", "Registrar alumno");
        return "alumnos/formulario";
    }

    // ---------- CREAR: POST /alumnos ----------
    @PostMapping
    public String crear(@ModelAttribute("alumno") Alumno alumno, Model model,
                        RedirectAttributes redirect) {
        try {
            alumnoService.crear(alumno);
            redirect.addFlashAttribute("mensaje", "Alumno registrado correctamente.");
            return "redirect:/alumnos";
        } catch (IllegalArgumentException e) {
            return regresarAlFormulario(alumno, null, "Registrar alumno", e.getMessage(), model);
        }
    }

    // ---------- ACTUALIZAR: formulario con datos ----------
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model,
                                   RedirectAttributes redirect) {
        return alumnoService.buscarPorId(id).map(alumno -> {
            model.addAttribute("alumno", alumno);
            model.addAttribute("titulo", "Editar alumno");
            return "alumnos/formulario";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("error", "No existe el alumno con id " + id + ".");
            return "redirect:/alumnos";
        });
    }

    // ---------- ACTUALIZAR: POST /alumnos/editar/{id} ----------
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Integer id, @ModelAttribute("alumno") Alumno alumno,
                             Model model, RedirectAttributes redirect) {
        try {
            boolean actualizado = alumnoService.actualizar(id, alumno).isPresent();
            if (actualizado) {
                redirect.addFlashAttribute("mensaje", "Alumno actualizado correctamente.");
            } else {
                redirect.addFlashAttribute("error", "No existe el alumno con id " + id + ".");
            }
            return "redirect:/alumnos";
        } catch (IllegalArgumentException e) {
            return regresarAlFormulario(alumno, id, "Editar alumno", e.getMessage(), model);
        }
    }

    // ---------- ELIMINAR: POST /alumnos/eliminar/{id} ----------
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirect) {
        if (alumnoService.eliminar(id)) {
            redirect.addFlashAttribute("mensaje", "Alumno eliminado correctamente.");
        } else {
            redirect.addFlashAttribute("error", "No existe el alumno con id " + id + ".");
        }
        return "redirect:/alumnos";
    }

    private String regresarAlFormulario(Alumno alumno, Integer id, String titulo,
                                        String error, Model model) {
        alumno.setId(id);
        if (alumno.getCarrera() == null) {
            alumno.setCarrera(new Carrera());
        }
        model.addAttribute("titulo", titulo);
        model.addAttribute("error", error);
        return "alumnos/formulario";
    }
}
