package com.grupobarreto.asistencia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistroResponse {
    private boolean success;
    private String mensaje;
    private RegistroAsistencia registro;
}
