package mx.edu.itoaxaca.smaact3.controllers.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.itoaxaca.smaact3.models.entity.Alumno;
import mx.edu.itoaxaca.smaact3.models.entity.Carrera;
import mx.edu.itoaxaca.smaact3.service.AlumnoService;
import mx.edu.itoaxaca.smaact3.service.CarreraService;

/**
 * API REST de Carreras (responde JSON).
 */
@RestController
@RequestMapping("/api/carreras")
public class CarreraRestController {

    private final CarreraService carreraService;
    private final AlumnoService alumnoService;

    public CarreraRestController(CarreraService carreraService, AlumnoService alumnoService) {
        this.carreraService = carreraService;
        this.alumnoService = alumnoService;
    }

    @GetMapping
    public List<Carrera> listar() {
        return carreraService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrera> obtener(@PathVariable Integer id) {
        return carreraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Relacion uno a muchos: alumnos de una carrera -> GET /api/carreras/{id}/alumnos
    @GetMapping("/{id}/alumnos")
    public ResponseEntity<List<Alumno>> alumnosDeCarrera(@PathVariable Integer id) {
        if (carreraService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(alumnoService.listarPorCarrera(id));
    }

    @PostMapping
    public ResponseEntity<Carrera> crear(@RequestBody Carrera carrera) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carreraService.crear(carrera));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carrera> actualizar(@PathVariable Integer id, @RequestBody Carrera carrera) {
        return carreraService.actualizar(id, carrera)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            return carreraService.eliminar(id)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }
}
