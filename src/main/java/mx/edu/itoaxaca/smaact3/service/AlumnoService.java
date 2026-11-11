package mx.edu.itoaxaca.smaact3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.itoaxaca.smaact3.models.entity.Alumno;
import mx.edu.itoaxaca.smaact3.models.entity.Carrera;
import mx.edu.itoaxaca.smaact3.repository.AlumnoRepository;
import mx.edu.itoaxaca.smaact3.repository.CarreraRepository;

/**
 * Capa de servicio de Alumno: contiene la logica de negocio del CRUD.
 * La usan tanto el controlador de vistas como el controlador REST.
 */
@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final CarreraRepository carreraRepository;

    public AlumnoService(AlumnoRepository alumnoRepository, CarreraRepository carreraRepository) {
        this.alumnoRepository = alumnoRepository;
        this.carreraRepository = carreraRepository;
    }

    // ---------- LEER ----------
    @Transactional(readOnly = true)
    public List<Alumno> listar() {
        return alumnoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Alumno> listarPorCarrera(Integer carreraId) {
        return alumnoRepository.findByCarreraId(carreraId);
    }

    @Transactional(readOnly = true)
    public Optional<Alumno> buscarPorId(Integer id) {
        return alumnoRepository.findById(id);
    }

    // ---------- CREAR ----------
    @Transactional
    public Alumno crear(Alumno alumno) {
        alumno.setId(null); // siempre es un registro nuevo
        asignarCarrera(alumno);
        return alumnoRepository.save(alumno);
    }

    // ---------- ACTUALIZAR ----------
    @Transactional
    public Optional<Alumno> actualizar(Integer id, Alumno datos) {
        return alumnoRepository.findById(id).map(existente -> {
            existente.setNumeroControl(datos.getNumeroControl());
            existente.setNombre(datos.getNombre());
            existente.setApellidos(datos.getApellidos());
            existente.setEmail(datos.getEmail());
            existente.setSemestre(datos.getSemestre());
            existente.setCarrera(datos.getCarrera());
            asignarCarrera(existente);
            return alumnoRepository.save(existente);
        });
    }

    // ---------- ELIMINAR ----------
    @Transactional
    public boolean eliminar(Integer id) {
        if (!alumnoRepository.existsById(id)) {
            return false;
        }
        alumnoRepository.deleteById(id);
        return true;
    }

    /**
     * Regla de negocio: todo alumno debe pertenecer a una carrera que exista.
     * Del formulario o del JSON solo llega el id de la carrera; aqui se busca
     * la carrera real en la base de datos y se asigna al alumno.
     */
    private void asignarCarrera(Alumno alumno) {
        if (alumno.getCarrera() == null || alumno.getCarrera().getId() == null) {
            throw new IllegalArgumentException("Debes seleccionar una carrera.");
        }
        Integer carreraId = alumno.getCarrera().getId();
        Carrera carrera = carreraRepository.findById(carreraId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La carrera con id " + carreraId + " no existe."));
        alumno.setCarrera(carrera);
    }
}
