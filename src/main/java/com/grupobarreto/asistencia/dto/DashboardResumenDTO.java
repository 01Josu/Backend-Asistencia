package com.grupobarreto.asistencia.dto;

public class DashboardResumenDTO {

    private long totalAsistencias;
    private long totalTardanzas;
    private long totalFaltas;
    private long totalJustificadas;

    public long getTotalAsistencias() {
        return totalAsistencias;
    }

    public void setTotalAsistencias(long totalAsistencias) {
        this.totalAsistencias = totalAsistencias;
    }

    public long getTotalTardanzas() {
        return totalTardanzas;
    }

    public void setTotalTardanzas(long totalTardanzas) {
        this.totalTardanzas = totalTardanzas;
    }

    public long getTotalFaltas() {
        return totalFaltas;
    }

    public void setTotalFaltas(long totalFaltas) {
        this.totalFaltas = totalFaltas;
    }

    public long getTotalJustificadas() {
        return totalJustificadas;
    }

    public void setTotalJustificadas(long totalJustificadas) {
        this.totalJustificadas = totalJustificadas;
    }
}