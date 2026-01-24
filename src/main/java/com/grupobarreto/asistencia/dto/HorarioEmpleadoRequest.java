package com.grupobarreto.asistencia.dto;

public class HorarioEmpleadoRequest {
    private Long idEmpleado;
    private Long idHorario;

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public HorarioEmpleadoRequest(Long idEmpleado, Long idHorario) {
        this.idEmpleado = idEmpleado;
        this.idHorario = idHorario;
    }
}

