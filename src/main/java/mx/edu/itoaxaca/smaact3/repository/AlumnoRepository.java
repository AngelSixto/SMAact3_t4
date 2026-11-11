package mx.edu.itoaxaca.smaact3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.itoaxaca.smaact3.models.entity.Alumno;

/**
 * Repositorio de Alumno. Ademas de los metodos heredados de JpaRepository,
 * Spring Data genera la consulta SQL a partir del NOMBRE del metodo.
 */
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

    // SELECT * FROM alumnos WHERE carrera_id = ?
    List<Alumno> findByCarreraId(Integer carreraId);

    // SELECT COUNT(*) > 0 FROM alumnos WHERE carrera_id = ?
    boolean existsByCarreraId(Integer carreraId);
}
