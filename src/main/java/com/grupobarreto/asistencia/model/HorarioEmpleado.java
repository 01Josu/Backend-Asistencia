package com.grupobarreto.asistencia.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "horario_empleado")
public class HorarioEmpleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHorarioEmpleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_horario")
    private Horario horario;


    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    
    public HorarioEmpleado() {
    }

    public Long getIdHorarioEmpleado() {
        return idHorarioEmpleado;
    }

    public void setIdHorarioEmpleado(Long idHorarioEmpleado) {
        this.idHorarioEmpleado = idHorarioEmpleado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}


