package com.grupobarreto.asistencia.model;

public class Empleado {
    private String id;
    private String apellido;
    private String nombre;
    private String usuario;
    private String contraseña;

    // Constructor
    public Empleado(String id, String apellido, String nombre, String usuario, String contraseña) {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    // Getters y setters
    public String getId() { return id; }
    public String getApellido() { return apellido; }
    public String getNombre() { return nombre; }
    public String getUsuario() { return usuario; }
    public String getContraseña() { return contraseña; }
}
