package com.grupobarreto.asistencia.dto;

public class JustificacionRequest {

    private Long idAsistencia;
    private String motivo;

    public JustificacionRequest() {
    }

    public Long getIdAsistencia() {
        return idAsistencia;
    }

    public void setIdAsistencia(Long idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
