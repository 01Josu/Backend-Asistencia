package com.grupobarreto.asistencia.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;

    @Column(unique = true)
    private String codigoEmpleado;

    private String nombres;
    private String apellidos;

    private boolean activo = true;

    private java.time.LocalDate fechaIngreso;

    public Empleado() {
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public boolean isActivo() {
        return activo;
    }

    public java.time.LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}
