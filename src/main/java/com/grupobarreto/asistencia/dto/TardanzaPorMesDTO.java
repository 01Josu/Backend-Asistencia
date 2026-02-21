package com.grupobarreto.asistencia.dto;

public class TardanzaPorMesDTO {

    private String mes;
    private long total;

    public TardanzaPorMesDTO(String mes, long total) {
        this.mes = mes;
        this.total = total;
    }

    public String getMes() {
        return mes;
    }

    public long getTotal() {
        return total;
    }
}