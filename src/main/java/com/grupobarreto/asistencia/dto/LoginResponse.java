package com.grupobarreto.asistencia.dto;

public class LoginResponse {

    private boolean success;
    private String mensaje;
    private Long idUsuario;
    private Long idEmpleado;
    private String nombres;
    private String apellidos;
    private String rol;
    private String token; // futuro JWT

    public LoginResponse(boolean success, String mensaje,
                         Long idUsuario, Long idEmpleado,
                         String nombres, String apellidos,
                         String rol, String token) {
        this.success = success;
        this.mensaje = mensaje;
        this.idUsuario = idUsuario;
        this.idEmpleado = idEmpleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.rol = rol;
        this.token = token;
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

    public String getRol() {
        return rol;
    }

    public String getToken() {
        return token;
    }
}
