package com.grupobarreto.asistencia.dto;

public class MarcarAsistenciaResponse {

    private boolean success;
    private String mensaje;
    private Long idAsistencia;
    private String estado;
    private String fecha;
    private String hora;
    private String tipo;

    // 👉 NUEVO
    private boolean requiereJustificacion;
    private String tipoJustificacion;

    public MarcarAsistenciaResponse() {}

    public MarcarAsistenciaResponse(
            boolean success,
            String mensaje,
            Long idAsistencia,
            String estado,
            String fecha,
            String hora,
            String tipo,
            boolean requiereJustificacion,
            String tipoJustificacion
    ) {
        this.success = success;
        this.mensaje = mensaje;
        this.idAsistencia = idAsistencia;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
        this.requiereJustificacion = requiereJustificacion;
        this.tipoJustificacion = tipoJustificacion;
    }

    public MarcarAsistenciaResponse(boolean success, String mensaje, Long idAsistencia, String estado, String fecha, String hora, String tipo) {
        this.success = success;
        this.mensaje = mensaje;
        this.idAsistencia = idAsistencia;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getIdAsistencia() {
        return idAsistencia;
    }

    public void setIdAsistencia(Long idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isRequiereJustificacion() {
        return requiereJustificacion;
    }

    public void setRequiereJustificacion(boolean requiereJustificacion) {
        this.requiereJustificacion = requiereJustificacion;
    }

    public String getTipoJustificacion() {
        return tipoJustificacion;
    }

    public void setTipoJustificacion(String tipoJustificacion) {
        this.tipoJustificacion = tipoJustificacion;
    }

    
}
