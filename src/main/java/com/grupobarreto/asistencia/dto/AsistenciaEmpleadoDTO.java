package com.grupobarreto.asistencia.dto;

public class AsistenciaEmpleadoDTO {

    private Long idEmpleado;
    private String nombres;
    private String apellidos;
    private long total;
    private long puntuales;
    private long tardanzas;
    private long faltas;

    public AsistenciaEmpleadoDTO(
            Long idEmpleado,
            String nombres,
            String apellidos,
            long total,
            long puntuales,
            long tardanzas,
            long faltas) {
        this.idEmpleado = idEmpleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.total = total;
        this.puntuales = puntuales;
        this.tardanzas = tardanzas;
        this.faltas = faltas;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public long getTotal() {
        return total;
    }

    public long getPuntuales() {
        return puntuales;
    }

    public long getTardanzas() {
        return tardanzas;
    }

    public long getFaltas() {
        return faltas;
    }
}