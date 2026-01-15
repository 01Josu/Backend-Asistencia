package com.grupobarreto.asistencia.dto;

public class LoginResponse {

    private boolean success;
    private String mensaje;
    private Long idUsuario;
    private Long idEmpleado;
    private String nombres;
    private String apellidos;

    public LoginResponse(boolean success, String mensaje,
                         Long idUsuario, Long idEmpleado,
                         String nombres, String apellidos) {
        this.success = success;
        this.mensaje = mensaje;
        this.idUsuario = idUsuario;
        this.idEmpleado = idEmpleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Long getIdUsuario() {
        return idUsuario;
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
}
