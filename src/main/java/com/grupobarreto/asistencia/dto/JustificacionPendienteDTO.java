package com.grupobarreto.asistencia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class JustificacionPendienteDTO {

    private Long idAsistencia;
    private LocalDate fecha;
    private LocalTime horaSalidaReal;
    private LocalTime horaSalidaEsperada;

    public JustificacionPendienteDTO() {
    }

    public JustificacionPendienteDTO(Long idAsistencia,
                                     LocalDate fecha,
                                     LocalTime horaSalidaReal,
                                     LocalTime horaSalidaEsperada) {
        this.idAsistencia = idAsistencia;
        this.fecha = fecha;
        this.horaSalidaReal = horaSalidaReal;
        this.horaSalidaEsperada = horaSalidaEsperada;
    }

    public Long getIdAsistencia() {
        return idAsistencia;
    }

    public void setIdAsistencia(Long idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraSalidaReal() {
        return horaSalidaReal;
    }

    public void setHoraSalidaReal(LocalTime horaSalidaReal) {
        this.horaSalidaReal = horaSalidaReal;
    }

    public LocalTime getHoraSalidaEsperada() {
        return horaSalidaEsperada;
    }

    public void setHoraSalidaEsperada(LocalTime horaSalidaEsperada) {
        this.horaSalidaEsperada = horaSalidaEsperada;
    }
}