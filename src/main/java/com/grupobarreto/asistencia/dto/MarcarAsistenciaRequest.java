package com.grupobarreto.asistencia.dto;

public class MarcarAsistenciaRequest {

    private Long idUsuario;

    public MarcarAsistenciaRequest() {
    }

    public MarcarAsistenciaRequest(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
