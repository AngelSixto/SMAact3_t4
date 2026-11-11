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
import mx.edu.itoaxaca.smaact3.service.AlumnoService;

/**
 * API REST del CRUD de Alumnos (responde JSON). Se prueba con Postman.
 * Usa el MISMO AlumnoService que el controlador de vistas.
 */
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoRestController {

    private final AlumnoService alumnoService;

    public AlumnoRestController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    // LEER todos: GET /api/alumnos
    @GetMapping
    public List<Alumno> listar() {
        return alumnoService.listar();
    }

    // LEER uno: GET /api/alumnos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> obtener(@PathVariable Integer id) {
        return alumnoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREAR: POST /api/alumnos   (body JSON con "carrera": {"id": 1})
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Alumno alumno) {
        try {
            Alumno nuevo = alumnoService.crear(alumno);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ACTUALIZAR: PUT /api/alumnos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Alumno alumno) {
        try {
            return alumnoService.actualizar(id, alumno)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ELIMINAR: DELETE /api/alumnos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return alumnoService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
