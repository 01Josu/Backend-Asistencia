package com.grupobarreto.asistencia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAsistencia {
    private String idEmpleado;
    private String nombre;
    private String fecha;
    private String hora;
    private String tipo; // Entrada o Salida
}
