package com.grupobarreto.asistencia.dto;

public class AsistenciaPorEstadoDTO {

    private String estado;
    private long total;

    public AsistenciaPorEstadoDTO(String estado, long total) {
        this.estado = estado;
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public long getTotal() {
        return total;
    }
}