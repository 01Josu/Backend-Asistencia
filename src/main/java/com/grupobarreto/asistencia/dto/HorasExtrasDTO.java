package com.grupobarreto.asistencia.dto;

public class HorasExtrasDTO {

    private Long idEmpleado;
    private String empleado;
    private String fecha;
    private String sobretiempo;
    private String motivo;

    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getEmpleado() { return empleado; }
    public void setEmpleado(String empleado) { this.empleado = empleado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getSobretiempo() { return sobretiempo; }
    public void setSobretiempo(String sobretiempo) { this.sobretiempo = sobretiempo; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}