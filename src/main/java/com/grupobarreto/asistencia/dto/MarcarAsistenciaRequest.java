package com.grupobarreto.asistencia.dto;

public class MarcarAsistenciaRequest {

    private Long idUsuario;
    private Double latitud;
    private Double longitud;

    public MarcarAsistenciaRequest() {
    }

    public MarcarAsistenciaRequest(Long idUsuario, Double latitud, Double longitud) {
        this.idUsuario = idUsuario;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
