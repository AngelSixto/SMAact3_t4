package mx.edu.itoaxaca.smaact3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.itoaxaca.smaact3.models.entity.Carrera;
import mx.edu.itoaxaca.smaact3.repository.AlumnoRepository;
import mx.edu.itoaxaca.smaact3.repository.CarreraRepository;

/**
 * Capa de servicio: aqui va la logica de negocio de Carrera.
 * Los controladores solo llaman a estos metodos.
 */
@Service
public class CarreraService {

    private final CarreraRepository carreraRepository;
    private final AlumnoRepository alumnoRepository;

    // Inyeccion de dependencias por constructor (Spring pasa los repositorios)
    public CarreraService(CarreraRepository carreraRepository, AlumnoRepository alumnoRepository) {
        this.carreraRepository = carreraRepository;
        this.alumnoRepository = alumnoRepository;
    }

    @Transactional(readOnly = true)
    public List<Carrera> listar() {
        return carreraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Carrera> buscarPorId(Integer id) {
        return carreraRepository.findById(id);
    }

    @Transactional
    public Carrera crear(Carrera carrera) {
        carrera.setId(null);
        return carreraRepository.save(carrera);
    }

    @Transactional
    public Optional<Carrera> actualizar(Integer id, Carrera datos) {
        return carreraRepository.findById(id).map(existente -> {
            existente.setClave(datos.getClave());
            existente.setNombre(datos.getNombre());
            return carreraRepository.save(existente);
        });
    }

    /**
     * Regla de negocio: no se puede borrar una carrera que tiene alumnos,
     * porque la llave foranea de esos alumnos quedaria apuntando a nada.
     */
    @Transactional
    public boolean eliminar(Integer id) {
        if (!carreraRepository.existsById(id)) {
            return false;
        }
        if (alumnoRepository.existsByCarreraId(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar la carrera porque tiene alumnos inscritos.");
        }
        carreraRepository.deleteById(id);
        return true;
    }
}
