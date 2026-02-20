package com.grupobarreto.asistencia.dto;

import java.time.LocalDate;
import com.grupobarreto.asistencia.model.TipoJustificacion;

public class JustificacionAdminDTO {

    private Long id;
    private String nombres;
    private String apellidos;
    private LocalDate fecha;
    private TipoJustificacion tipo;   // 👈 CAMBIO AQUÍ
    private String motivo;
    private Boolean aprobado;

    public JustificacionAdminDTO(Long id,
                                 String nombres,
                                 String apellidos,
                                 LocalDate fecha,
                                 TipoJustificacion tipo,  // 👈 CAMBIO AQUÍ
                                 String motivo,
                                 Boolean aprobado) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fecha = fecha;
        this.tipo = tipo;
        this.motivo = motivo;
        this.aprobado = aprobado;
    }

    public Long getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public LocalDate getFecha() { return fecha; }
    public TipoJustificacion getTipo() { return tipo; }
    public String getMotivo() { return motivo; }
    public Boolean getAprobado() { return aprobado; }
}