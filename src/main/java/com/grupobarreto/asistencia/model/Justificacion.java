package com.grupobarreto.asistencia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "justificacion")
public class Justificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJustificacion;

    @OneToOne
    @JoinColumn(name = "id_asistencia")
    private Asistencia asistencia;

    private String motivo;
    private Boolean aprobado;

    public Justificacion() {
    }

    public Long getIdJustificacion() {
        return idJustificacion;
    }

    public void setIdJustificacion(Long idJustificacion) {
        this.idJustificacion = idJustificacion;
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }
}
