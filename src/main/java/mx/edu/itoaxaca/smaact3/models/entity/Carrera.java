package mx.edu.itoaxaca.smaact3.models.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad Carrera -> tabla "carreras".
 * Lado "uno" de la relacion: UNA carrera tiene MUCHOS alumnos (@OneToMany).
 */
@Entity
@Table(name = "carreras")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 10)
    private String clave;

    @Column(nullable = false, length = 100)
    private String nombre;

    // mappedBy = "carrera": la llave foranea la administra el atributo "carrera" de Alumno.
    // @JsonIgnore evita un ciclo infinito al convertir a JSON (Carrera -> Alumnos -> Carrera -> ...)
    @OneToMany(mappedBy = "carrera")
    @JsonIgnore
    private List<Alumno> alumnos = new ArrayList<>();

    public Carrera() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }
}
