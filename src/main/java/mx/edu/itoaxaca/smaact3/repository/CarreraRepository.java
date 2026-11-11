package mx.edu.itoaxaca.smaact3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.itoaxaca.smaact3.models.entity.Carrera;

/**
 * Al extender JpaRepository<Carrera, Integer> ya tenemos, sin escribir SQL:
 * findAll(), findById(), save(), deleteById(), existsById(), count()...
 * Carrera = entidad que maneja, Integer = tipo de su llave primaria.
 */
public interface CarreraRepository extends JpaRepository<Carrera, Integer> {
}
