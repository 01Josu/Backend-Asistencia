package com.grupobarreto.asistencia.dto;

public class AsistenciaExcelDTO {

    private String fecha;
    private String horaEntradaReal;
    private String horaIngreso;        // horario esperado ese día
    private String tardanza;
    private String marcaTardanza;
    private String horaSalida;
    private String horasLaboradas;
    private String horasDiarias;
    private String extra;
    private String pendiente;
    private String empleado;
    private String descripcionHorario; // <-- importante para mostrar si fue sábado
    
    private String horaEntradaProgramada;
    private String horaSalidaProgramada;

    public String getHoraEntradaProgramada() { return horaEntradaProgramada; }
    public void setHoraEntradaProgramada(String horaEntradaProgramada) { this.horaEntradaProgramada = horaEntradaProgramada; }

    public String getHoraSalidaProgramada() { return horaSalidaProgramada; }
    public void setHoraSalidaProgramada(String horaSalidaProgramada) { this.horaSalidaProgramada = horaSalidaProgramada; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHoraEntradaReal() { return horaEntradaReal; }
    public void setHoraEntradaReal(String horaEntradaReal) { this.horaEntradaReal = horaEntradaReal; }

    public String getHoraIngreso() { return horaIngreso; }
    public void setHoraIngreso(String horaIngreso) { this.horaIngreso = horaIngreso; }

    public String getTardanza() { return tardanza; }
    public void setTardanza(String tardanza) { this.tardanza = tardanza; }

    public String getMarcaTardanza() { return marcaTardanza; }
    public void setMarcaTardanza(String marcaTardanza) { this.marcaTardanza = marcaTardanza; }

    public String getHoraSalida() { return horaSalida; }
    public void setHoraSalida(String horaSalida) { this.horaSalida = horaSalida; }

    public String getHorasLaboradas() { return horasLaboradas; }
    public void setHorasLaboradas(String horasLaboradas) { this.horasLaboradas = horasLaboradas; }

    public String getHorasDiarias() { return horasDiarias; }
    public void setHorasDiarias(String horasDiarias) { this.horasDiarias = horasDiarias; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }

    public String getPendiente() { return pendiente; }
    public void setPendiente(String pendiente) { this.pendiente = pendiente; }

    public String getEmpleado() { return empleado; }
    public void setEmpleado(String empleado) { this.empleado = empleado; }

    public String getDescripcionHorario() { return descripcionHorario; }
    public void setDescripcionHorario(String descripcionHorario) { this.descripcionHorario = descripcionHorario; }
}