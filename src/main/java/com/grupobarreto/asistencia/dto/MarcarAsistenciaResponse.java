package com.grupobarreto.asistencia.dto;

public class MarcarAsistenciaResponse {

    private boolean success;
    private String mensaje;
    private String fecha;
    private String hora;
    private String tipo;

    public MarcarAsistenciaResponse() {
    }

    public MarcarAsistenciaResponse(boolean success, String mensaje, String fecha, String hora, String tipo) {
        this.success = success;
        this.mensaje = mensaje;
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
}
